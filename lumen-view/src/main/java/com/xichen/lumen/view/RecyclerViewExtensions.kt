package com.xichen.lumen.view

import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView 扩展函数
 * 
 * 用于在 RecyclerView 中安全使用 Lumen 图片加载
 * 自动处理 ViewHolder 复用时的任务取消
 */

/**
 * 在 RecyclerView.Adapter 的 onViewRecycled 中调用
 * 自动取消该 ViewHolder 中 ImageView 的加载任务
 * 
 * 使用示例：
 * ```
 * override fun onViewRecycled(holder: ViewHolder) {
 *     super.onViewRecycled(holder)
 *     holder.itemView.cancelLumenLoad()
 * }
 * ```
 */
fun RecyclerView.ViewHolder.cancelLumenLoad() {
    itemView.cancelLumenLoad()
}

/**
 * 取消 View 及其子 View 中所有 Lumen 加载任务
 */
fun android.view.View.cancelLumenLoad() {
    // 如果当前 View 是 ImageView，取消其加载任务
    if (this is android.widget.ImageView) {
        ImageViewTarget.from(this)?.cancel()
    }
    
    // 递归处理子 View
    if (this is android.view.ViewGroup) {
        for (i in 0 until childCount) {
            getChildAt(i).cancelLumenLoad()
        }
    }
}

