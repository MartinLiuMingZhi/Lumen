package com.xichen.lumen.view

import com.xichen.lumen.transform.BlurTransformer
import com.xichen.lumen.transform.CropMode
import com.xichen.lumen.transform.CropTransformer
import com.xichen.lumen.transform.RotateTransformer
import com.xichen.lumen.transform.RoundedCornersTransformer

/**
 * Lumen Transform 扩展函数
 * 
 * 提供便捷的 DSL API 用于添加图片转换器
 * 这些扩展函数需要在 lumen-view 模块中，因为它们扩展了 RequestBuilder
 */

/**
 * 添加圆角转换器
 * 
 * @param radius 统一圆角半径（像素）
 */
fun RequestBuilder.roundedCorners(radius: Float) {
    addTransformer(RoundedCornersTransformer(radius))
}

/**
 * 添加圆角转换器（分别设置四个角）
 */
fun RequestBuilder.roundedCorners(
    topLeft: Float = 0f,
    topRight: Float = 0f,
    bottomRight: Float = 0f,
    bottomLeft: Float = 0f
) {
    addTransformer(RoundedCornersTransformer(
        topLeft = topLeft,
        topRight = topRight,
        bottomRight = bottomRight,
        bottomLeft = bottomLeft
    ))
}

/**
 * 添加旋转转换器
 * 
 * @param degrees 旋转角度（顺时针为正）
 */
fun RequestBuilder.rotate(degrees: Float) {
    addTransformer(RotateTransformer(degrees))
}

/**
 * 添加裁剪转换器
 * 
 * @param mode 裁剪模式
 * @param width 目标宽度
 * @param height 目标高度
 */
fun RequestBuilder.crop(mode: CropMode, width: Int, height: Int) {
    addTransformer(CropTransformer(mode = mode, width = width, height = height))
}

/**
 * 添加裁剪转换器（自定义区域）
 * 
 * @param x 起始 X 坐标
 * @param y 起始 Y 坐标
 * @param width 裁剪宽度
 * @param height 裁剪高度
 */
fun RequestBuilder.crop(x: Int, y: Int, width: Int, height: Int) {
    addTransformer(CropTransformer(x = x, y = y, width = width, height = height))
}

/**
 * 添加模糊转换器
 * 
 * @param radius 模糊半径（0-25）
 * @param scale 缩放比例（0-1，用于性能优化）
 */
fun RequestBuilder.blur(radius: Float = 10f, scale: Float = 1f) {
    addTransformer(BlurTransformer(radius = radius, scale = scale))
}

