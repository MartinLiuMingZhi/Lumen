package com.xichen.lumen.core

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Lumen 图片加载器核心类
 * 
 * Lumen 是一个 Kotlin-first 的图片加载库，面向业务友好、AI 场景、列表场景。
 * 强调状态可控、兜底清晰、链路透明。
 * 
 * 核心职责：
 * - 调度加载流程（Fetcher → Decryptor → Decoder → Transformer → Cache）
 * - 管理协程生命周期
 * - 缓存协调（内存缓存）
 * 
 * 使用示例：
 * ```
 * val lumen = Lumen.with(context)
 * lumen.load(request).collect { state ->
 *     when (state) {
 *         is ImageState.Success -> imageView.setImageBitmap(state.bitmap)
 *         is ImageState.Error -> // 处理错误
 *         else -> // 处理其他状态
 *     }
 * }
 * ```
 */
class Lumen private constructor(
    private val context: Context,
    private val memoryCache: MemoryCache
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * 加载图片
     * @param request 图片加载请求
     * @return 图片状态 Flow
     */
    fun load(request: ImageRequest): Flow<ImageState> = flow {
        // 1. 检查内存缓存
        val cachedBitmap = memoryCache.get(request.cacheKey)
        if (cachedBitmap != null) {
            emit(ImageState.Success(cachedBitmap))
            return@flow
        }

        // 2. 发送加载中状态
        emit(ImageState.Loading)

        try {
            // 3. 获取数据
            val fetcher = FetcherFactory.create(context, request.data)
            var data = withContext(Dispatchers.IO) {
                fetcher.fetch()
            }

            // 4. 解密（如果需要）
            request.decryptor?.let { decryptor ->
                data = withContext(Dispatchers.Default) {
                    decryptor.decrypt(data)
                }
            }

            // 5. 解码
            var bitmap = withContext(Dispatchers.Default) {
                Decoder.decode(data)
            }

            // 6. 转换（如果需要）
            request.transformers.forEach { transformer ->
                bitmap = withContext(Dispatchers.Default) {
                    transformer.transform(bitmap)
                }
            }

            // 7. 存入缓存
            memoryCache.put(request.cacheKey, bitmap)

            // 8. 发送成功状态
            emit(ImageState.Success(bitmap))
        } catch (e: Exception) {
            // 9. 发送错误状态
            emit(ImageState.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 取消所有加载任务
     */
    fun cancelAll() {
        scope.cancel()
    }

    /**
     * 清空内存缓存
     */
    fun clearCache() {
        memoryCache.clear()
    }

    companion object {
        @Volatile
        private var INSTANCE: Lumen? = null

        /**
         * 获取单例实例
         */
        fun getInstance(context: Context): Lumen {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Lumen(
                    context.applicationContext,
                    MemoryCache()
                ).also { INSTANCE = it }
            }
        }

        /**
         * 创建新实例（用于测试或特殊场景）
         */
        fun create(context: Context, memoryCache: MemoryCache = MemoryCache()): Lumen {
            return Lumen(context.applicationContext, memoryCache)
        }

        /**
         * 便捷方法：获取 Lumen 实例
         * 用于 DSL API：Lumen.with(context)
         */
        fun with(context: Context): Lumen {
            return getInstance(context)
        }
    }
}

