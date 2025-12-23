package com.xichen.lumen.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.xichen.lumen.core.ImageData
import com.xichen.lumen.core.Lumen
import java.io.File

/**
 * Lumen DSL API 扩展函数
 */

/**
 * 加载 URL
 */
fun Lumen.load(url: String, block: RequestBuilderScope? = null): RequestBuilder {
    val builder = RequestBuilder(this, ImageData.Url(url))
    block?.invoke(builder)
    return builder
}

/**
 * 加载文件
 */
fun Lumen.load(file: File, block: RequestBuilderScope? = null): RequestBuilder {
    val builder = RequestBuilder(this, ImageData.File(file))
    block?.invoke(builder)
    return builder
}

/**
 * 加载 Uri
 */
fun Lumen.load(uri: Uri, block: RequestBuilderScope? = null): RequestBuilder {
    val builder = RequestBuilder(this, ImageData.Uri(uri))
    block?.invoke(builder)
    return builder
}

/**
 * 加载资源 ID
 */
fun Lumen.load(resId: Int, block: RequestBuilderScope? = null): RequestBuilder {
    val builder = RequestBuilder(this, ImageData.Resource(resId))
    block?.invoke(builder)
    return builder
}

/**
 * 加载 ImageData
 */
fun Lumen.load(data: ImageData, block: RequestBuilderScope? = null): RequestBuilder {
    val builder = RequestBuilder(this, data)
    block?.invoke(builder)
    return builder
}

/**
 * 加载视频文件（提取第一帧）
 */
fun Lumen.loadVideo(file: File, block: RequestBuilderScope? = null): RequestBuilder {
    val builder = RequestBuilder(this, ImageData.Video(file))
    block?.invoke(builder)
    return builder
}

/**
 * 加载视频文件（提取指定时间点的帧）
 * @param file 视频文件
 * @param timeUs 时间点（微秒），例如 5_000_000L 表示 5 秒
 */
fun Lumen.loadVideo(file: File, timeUs: Long, block: RequestBuilderScope? = null): RequestBuilder {
    val builder = RequestBuilder(this, ImageData.Video(file, timeUs))
    block?.invoke(builder)
    return builder
}

/**
 * 加载视频 Uri（提取第一帧）
 */
fun Lumen.loadVideo(uri: Uri, block: RequestBuilderScope? = null): RequestBuilder {
    val builder = RequestBuilder(this, ImageData.VideoUri(uri))
    block?.invoke(builder)
    return builder
}

/**
 * 加载视频 Uri（提取指定时间点的帧）
 * @param uri 视频 Uri
 * @param timeUs 时间点（微秒），例如 5_000_000L 表示 5 秒
 */
fun Lumen.loadVideo(uri: Uri, timeUs: Long, block: RequestBuilderScope? = null): RequestBuilder {
    val builder = RequestBuilder(this, ImageData.VideoUri(uri, timeUs))
    block?.invoke(builder)
    return builder
}


/**
 * 将请求应用到 ImageView
 */
fun RequestBuilder.into(imageView: ImageView) {
    val context = imageView.context
    
    // 获取或创建 ImageViewTarget
    val target = ImageViewTarget.from(imageView) 
        ?: ImageViewTarget(imageView, this.lumen)
    
    // 构建请求
    val request = build(context)
    
    target.load(request)
}

