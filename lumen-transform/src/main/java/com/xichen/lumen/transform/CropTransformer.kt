package com.xichen.lumen.transform

import android.graphics.Bitmap
import com.xichen.lumen.core.BitmapTransformer

/**
 * Lumen 裁剪转换器
 * 
 * 对 Bitmap 进行裁剪处理，支持多种裁剪模式
 * 
 * 使用示例：
 * ```
 * CropTransformer(CropMode.CENTER_CROP) // 居中裁剪
 * CropTransformer(CropMode.TOP_CENTER) // 顶部居中裁剪
 * CropTransformer(x = 10, y = 20, width = 100, height = 100) // 自定义裁剪区域
 * ```
 */
class CropTransformer(
    private val mode: CropMode? = null,
    private val x: Int = 0,
    private val y: Int = 0,
    private val width: Int = 0,
    private val height: Int = 0
) : BitmapTransformer {

    init {
        if (mode != null) {
            require(width > 0 && height > 0) {
                "When mode is not null, width and height must be positive"
            }
        } else {
            require(x >= 0 && y >= 0 && width > 0 && height > 0) {
                "When mode is null, x, y, width, height must be valid"
            }
        }
    }

    override fun transform(source: Bitmap): Bitmap {
        val sourceWidth = source.width
        val sourceHeight = source.height
        
        return when (mode) {
            CropMode.CENTER_CROP -> {
                // 计算缩放比例，使图片能够覆盖目标尺寸
                val scale = maxOf(
                    width.toFloat() / sourceWidth,
                    height.toFloat() / sourceHeight
                )
                val scaledWidth = (sourceWidth * scale).toInt()
                val scaledHeight = (sourceHeight * scale).toInt()
                // 从缩放后的图片中心裁剪
                val startX = (scaledWidth - width) / 2
                val startY = (scaledHeight - height) / 2
                // 先缩放，再裁剪
                val scaled = Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true)
                return Bitmap.createBitmap(scaled, startX, startY, width, height)
            }
            CropMode.CENTER_INSIDE -> {
                // 计算缩放比例，使图片能够完整显示在目标尺寸内
                val scale = minOf(
                    width.toFloat() / sourceWidth,
                    height.toFloat() / sourceHeight
                )
                val scaledWidth = (sourceWidth * scale).toInt()
                val scaledHeight = (sourceHeight * scale).toInt()
                return Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true)
            }
            CropMode.TOP_CENTER -> {
                val scale = maxOf(
                    width.toFloat() / sourceWidth,
                    height.toFloat() / sourceHeight
                )
                val scaledWidth = (sourceWidth * scale).toInt()
                val scaledHeight = (sourceHeight * scale).toInt()
                val startX = (scaledWidth - width) / 2
                val scaled = Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true)
                return Bitmap.createBitmap(scaled, startX, 0, width, height)
            }
            CropMode.BOTTOM_CENTER -> {
                val scale = maxOf(
                    width.toFloat() / sourceWidth,
                    height.toFloat() / sourceHeight
                )
                val scaledWidth = (sourceWidth * scale).toInt()
                val scaledHeight = (sourceHeight * scale).toInt()
                val startX = (scaledWidth - width) / 2
                val startY = scaledHeight - height
                val scaled = Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true)
                return Bitmap.createBitmap(scaled, startX, startY, width, height)
            }
            null -> {
                // 自定义裁剪区域
                require(x >= 0 && y >= 0 && x + width <= sourceWidth && y + height <= sourceHeight) {
                    "Crop area is out of bounds"
                }
                Bitmap.createBitmap(source, x, y, width, height)
            }
        }
    }

    override val key: String
        get() = buildString {
            append("crop")
            if (mode != null) {
                append("_").append(mode.name)
            } else {
                append("_x").append(x)
                append("_y").append(y)
                append("_w").append(width)
                append("_h").append(height)
            }
        }
}

/**
 * 裁剪模式
 */
enum class CropMode {
    /**
     * 居中裁剪（类似 ImageView 的 CENTER_CROP）
     * 保持宽高比，裁剪多余部分
     */
    CENTER_CROP,

    /**
     * 居中内嵌（类似 ImageView 的 CENTER_INSIDE）
     * 保持宽高比，完整显示图片
     */
    CENTER_INSIDE,

    /**
     * 顶部居中裁剪
     */
    TOP_CENTER,

    /**
     * 底部居中裁剪
     */
    BOTTOM_CENTER
}

