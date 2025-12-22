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

