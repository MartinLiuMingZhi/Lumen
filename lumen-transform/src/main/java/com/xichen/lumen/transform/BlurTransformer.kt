package com.xichen.lumen.transform

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.xichen.lumen.core.BitmapTransformer

/**
 * Lumen 模糊转换器
 * 
 * 对 Bitmap 进行高斯模糊处理
 * 
 * @param radius 模糊半径（0-25，值越大越模糊）
 * @param scale 缩放比例（0-1，用于性能优化，值越小性能越好但模糊效果可能降低）
 * 
 * 使用示例：
 * ```
 * BlurTransformer(radius = 10f) // 模糊半径 10
 * BlurTransformer(radius = 15f, scale = 0.5f) // 模糊半径 15，缩放 0.5 倍以提升性能
 * ```
 */
class BlurTransformer(
    private val radius: Float = 10f,
    private val scale: Float = 1f
) : BitmapTransformer {

    init {
        require(radius in 0f..25f) {
            "Blur radius must be between 0 and 25"
        }
        require(scale in 0f..1f) {
            "Scale must be between 0 and 1"
        }
    }

    override fun transform(source: Bitmap): Bitmap {
        if (radius == 0f) {
            return source
        }
        
        // 如果 scale < 1，先缩放图片以提升性能
        val scaledBitmap = if (scale < 1f) {
            val scaledWidth = (source.width * scale).toInt().coerceAtLeast(1)
            val scaledHeight = (source.height * scale).toInt().coerceAtLeast(1)
            Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true)
        } else {
            source
        }
        
        // 使用 BlurMaskFilter 进行模糊（兼容性好）
        val blurred = simpleBlur(scaledBitmap, radius)
        
        // 如果进行了缩放，需要将结果缩放回原始尺寸
        return if (scale < 1f && blurred.width != source.width) {
            Bitmap.createScaledBitmap(blurred, source.width, source.height, true)
        } else {
            blurred
        }
    }

    private fun simpleBlur(bitmap: Bitmap, radius: Float): Bitmap {
        // 简单的盒状模糊实现（性能较差，但兼容性好）
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        paint.maskFilter = android.graphics.BlurMaskFilter(radius, android.graphics.BlurMaskFilter.Blur.NORMAL)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return output
    }

    override val key: String
        get() = "blur_r${radius}_s${scale}"
}

