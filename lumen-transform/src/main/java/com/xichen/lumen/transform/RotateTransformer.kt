package com.xichen.lumen.transform

import android.graphics.Bitmap
import android.graphics.Matrix
import com.xichen.lumen.core.BitmapTransformer

/**
 * Lumen 旋转转换器
 * 
 * 对 Bitmap 进行旋转处理
 * 
 * @param degrees 旋转角度（顺时针为正，逆时针为负）
 * 
 * 使用示例：
 * ```
 * RotateTransformer(90f) // 顺时针旋转 90 度
 * RotateTransformer(-45f) // 逆时针旋转 45 度
 * ```
 */
class RotateTransformer(
    private val degrees: Float
) : BitmapTransformer {

    override fun transform(source: Bitmap): Bitmap {
        if (degrees == 0f) {
            return source
        }
        
        val matrix = Matrix().apply {
            postRotate(degrees)
        }
        
        return Bitmap.createBitmap(
            source,
            0,
            0,
            source.width,
            source.height,
            matrix,
            true
        )
    }

    override val key: String
        get() = "rotate_${degrees}"
}

