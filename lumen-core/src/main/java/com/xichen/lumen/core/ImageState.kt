package com.xichen.lumen.core

import android.graphics.Bitmap

/**
 * Lumen 图片加载状态模型
 * 
 * 用于表示图片加载过程中的不同状态，支持 AI UI、多状态渲染和调试
 */
sealed class ImageState {
    /**
     * 加载中状态
     */
    object Loading : ImageState()

    /**
     * 加载成功状态
     * @param bitmap 加载成功的 Bitmap
     */
    data class Success(val bitmap: Bitmap) : ImageState()

    /**
     * 加载失败状态
     * @param throwable 失败原因（可选）
     */
    data class Error(val throwable: Throwable? = null) : ImageState()

    /**
     * 兜底状态（当加载失败时使用备用方案）
     */
    object Fallback : ImageState()
}

