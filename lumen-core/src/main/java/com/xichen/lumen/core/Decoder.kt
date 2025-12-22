package com.xichen.lumen.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Lumen 图片解码器
 * 
 * 负责将字节数组解码为 Bitmap，使用 BitmapFactory 实现
 */
object Decoder {
    /**
     * 解码字节数组为 Bitmap
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

