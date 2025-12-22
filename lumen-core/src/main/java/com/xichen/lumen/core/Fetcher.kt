package com.xichen.lumen.core

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Lumen 数据获取器接口
 * 
 * 负责从不同数据源获取原始字节数据
 */
interface Fetcher {
    /**
     * 获取数据
     * @return 原始字节数组
     */
    suspend fun fetch(): ByteArray

    /**
     * 缓存 Key
     */
    val key: String
}

/**
 * Lumen 网络数据获取器
 * 
 * 从网络 URL 获取图片数据
 */
class NetworkFetcher(
    private val url: String,
    private val connectTimeout: Int = 10000,
    private val readTimeout: Int = 10000
) : Fetcher {
    override val key: String = "network:$url"

    override suspend fun fetch(): ByteArray = withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = connectTimeout
            connection.readTimeout = readTimeout
            connection.requestMethod = "GET"
            
            // 设置 User-Agent，避免某些服务器拒绝请求
            connection.setRequestProperty("User-Agent", "Lumen-ImageLoader/1.0")
            connection.setRequestProperty("Accept", "image/*")
            
            // 允许重定向
            connection.instanceFollowRedirects = true
            
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                val errorMessage = try {
                    connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error"
                } catch (e: Exception) {
                    "Failed to read error stream"
                }
                throw IllegalStateException("HTTP error code: $responseCode, message: $errorMessage")
            }

            connection.inputStream.use { input ->
                input.readBytes()
            }
        } catch (e: Exception) {
            android.util.Log.e("Lumen", "Failed to fetch image from $url", e)
            throw e
        } finally {
            connection.disconnect()
        }
    }
}

/**
 * Lumen 文件数据获取器
 * 
 * 从本地文件系统获取图片数据
 */
class FileFetcher(
    private val file: File
) : Fetcher {
    override val key: String = "file:${file.absolutePath}"

    override suspend fun fetch(): ByteArray = withContext(Dispatchers.IO) {
        if (!file.exists()) {
            throw IllegalStateException("File not found: ${file.absolutePath}")
        }
        FileInputStream(file).use { input ->
            input.readBytes()
        }
    }
}

/**
 * Lumen Uri 数据获取器
 * 
 * 从 Android ContentProvider Uri 获取图片数据
 */
class UriFetcher(
    private val context: Context,
    private val uri: Uri
) : Fetcher {
    override val key: String = "uri:$uri"

    override suspend fun fetch(): ByteArray = withContext(Dispatchers.IO) {
        context.contentResolver.openInputStream(uri)?.use { input ->
            input.readBytes()
        } ?: throw IllegalStateException("Cannot open URI: $uri")
    }
}

/**
 * Lumen 资源数据获取器
 * 
 * 从 Android 资源文件获取图片数据
 */
class ResourceFetcher(
    private val context: Context,
    private val resId: Int
) : Fetcher {
    override val key: String = "res:$resId"

    override suspend fun fetch(): ByteArray = withContext(Dispatchers.IO) {
        context.resources.openRawResource(resId).use { input ->
            input.readBytes()
        }
    }
}

/**
 * Lumen Fetcher 工厂
 * 
 * 根据 ImageData 类型创建对应的 Fetcher 实例
 */
object FetcherFactory {
    fun create(context: Context, data: ImageData): Fetcher {
        return when (data) {
            is ImageData.Url -> NetworkFetcher(data.url)
            is ImageData.File -> FileFetcher(data.file)
            is ImageData.Uri -> UriFetcher(context, data.uri)
            is ImageData.Resource -> ResourceFetcher(context, data.resId)
        }
    }
}

