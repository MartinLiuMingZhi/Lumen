package com.xichen.lumen.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer

/**
 * Lumen 图片解码器
 * 
 * 负责将字节数组解码为 Bitmap 或 Drawable，支持静态图片和 GIF 动画
 */
object Decoder {
    /**
     * 解码字节数组为 Bitmap（静态图片）
     * @param data 图片字节数据
     * @param options 解码选项（可选）
     * @return 解码后的 Bitmap
     */
    suspend fun decode(
        data: ByteArray,
        options: BitmapFactory.Options? = null
    ): Bitmap = withContext(Dispatchers.Default) {
        val bitmap = options?.let {
            BitmapFactory.decodeByteArray(data, 0, data.size, it)
        } ?: BitmapFactory.decodeByteArray(data, 0, data.size)

        bitmap ?: throw IllegalStateException("Failed to decode bitmap from byte array")
    }

    /**
     * 解码字节数组为 Drawable（支持 GIF 动画）
     * @param context Context
     * @param data 图片字节数据
     * @return 解码后的 Drawable（如果是 GIF，返回 AnimatedImageDrawable）
     */
    suspend fun decodeAnimated(
        context: Context,
        data: ByteArray
    ): Drawable = withContext(Dispatchers.Default) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // API 28+ 使用 ImageDecoder 支持 GIF 动画
            val byteBuffer = ByteBuffer.wrap(data)
            val source = ImageDecoder.createSource(byteBuffer)
            ImageDecoder.decodeDrawable(source)
        } else {
            // API < 28 降级为静态图片（第一帧）
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                ?: throw IllegalStateException("Failed to decode image from byte array")
            android.graphics.drawable.BitmapDrawable(
                context.resources,
                bitmap
            )
        }
    }

    /**
     * 检测是否为 GIF 格式
     * @param data 图片字节数据
     * @return 是否为 GIF
     */
    fun isGif(data: ByteArray): Boolean {
        if (data.size < 6) return false
        // GIF 文件头：GIF87a 或 GIF89a
        val header = String(data, 0, 6)
        return header == "GIF87a" || header == "GIF89a"
    }

    /**
     * 创建默认解码选项
     * @param inSampleSize 采样率（用于内存优化）
     * @return 解码选项
     */
    fun createOptions(inSampleSize: Int = 1): BitmapFactory.Options {
        return BitmapFactory.Options().apply {
            this.inSampleSize = inSampleSize
        }
    }
}

