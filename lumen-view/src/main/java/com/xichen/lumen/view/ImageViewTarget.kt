package com.xichen.lumen.view

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.xichen.lumen.core.ImageRequest
import com.xichen.lumen.core.ImageState
import com.xichen.lumen.core.Lumen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * ImageView Target
 * 
 * 负责将图片加载结果应用到 ImageView
 * 支持 RecyclerView 场景：自动取消旧任务、占位图立即显示
 */
class ImageViewTarget(
    private val imageView: ImageView,
    private val lumen: Lumen
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var currentJob: Job? = null

    init {
        // 绑定 ImageViewTarget 到 View tag，用于 RecyclerView 场景
        imageView.setTag(R.id.lumen_load_job, this)
    }

    /**
     * 加载图片
     */
    fun load(request: ImageRequest) {
        // 取消之前的加载任务
        cancel()

        // 立即显示占位图
        request.placeholder?.let {
            imageView.setImageDrawable(it)
        }

        // 开始加载
        currentJob = scope.launch {
            try {
                lumen.load(request)
                    .collect { state ->
                        when (state) {
                            is ImageState.Loading -> {
                                // 加载中，占位图已在上面显示
                                android.util.Log.d("Lumen", "Loading image: ${request.data.key}")
                            }
                            is ImageState.Success -> {
                                android.util.Log.d("Lumen", "Successfully loaded image: ${request.data.key}, size: ${state.bitmap.width}x${state.bitmap.height}")
                                imageView.setImageBitmap(state.bitmap)
                            }
                            is ImageState.Error -> {
                                // 打印错误日志
                                android.util.Log.e("Lumen", "Failed to load image: ${request.data.key}", state.throwable)
                                // 显示错误图片
                                request.error?.let {
                                    imageView.setImageDrawable(it)
                                }
                            }
                            is ImageState.Fallback -> {
                                // 显示兜底 UI（由外部处理）
                                android.util.Log.w("Lumen", "Fallback for image: ${request.data.key}")
                            }
                        }
                    }
            } catch (e: Exception) {
                android.util.Log.e("Lumen", "Exception in image loading", e)
                request.error?.let {
                    imageView.setImageDrawable(it)
                }
            }
        }
    }

    /**
     * 取消加载
     */
    fun cancel() {
        currentJob?.cancel()
        currentJob = null
    }

    /**
     * 清理资源
     */
    fun cleanup() {
        cancel()
        scope.cancel()
        imageView.setTag(R.id.lumen_load_job, null)
    }

    companion object {
        /**
         * 从 View 获取绑定的 ImageViewTarget
         */
        @JvmStatic
        fun from(view: View): ImageViewTarget? {
            return view.getTag(R.id.lumen_load_job) as? ImageViewTarget
        }
    }
}

