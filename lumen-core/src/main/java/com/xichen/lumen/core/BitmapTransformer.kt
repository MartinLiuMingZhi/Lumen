package com.xichen.lumen.core

import android.graphics.Bitmap

/**
 * Lumen Bitmap 转换器接口
 * 
 * 用于对 Bitmap 进行各种转换操作，如圆角、裁剪、模糊等
 * 支持链式转换，每个转换器都有唯一的 key 用于缓存区分
 */
interface BitmapTransformer {
    /**
     * 转换 Bitmap
     * @param source 原始 Bitmap
     * @return 转换后的 Bitmap
     */
    fun transform(source: Bitmap): Bitmap

    /**
     * 缓存 Key（用于区分不同的转换器）
     */
    val key: String
}

