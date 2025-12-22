package com.xichen.lumen.ui.components

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xichen.lumen.core.Lumen
import com.xichen.lumen.view.*

@Composable
fun RecyclerViewSection() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "RecyclerView 示例",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            
            AndroidView(
                factory = { ctx ->
                    RecyclerView(ctx).apply {
                        layoutManager = LinearLayoutManager(ctx)
                        adapter = ImageAdapter(
                            images = (1..10).map { "https://image.liumingzhi.cn/file/4c26ab1b462e8dd701151.png" }
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            )
            
            Text(
                text = "自动处理 ViewHolder 复用，在 onViewRecycled 中取消加载任务",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )
        }
    }
}

class ImageAdapter(private val images: List<String>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    
    class ViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (200 * parent.resources.displayMetrics.density).toInt()
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return ViewHolder(imageView)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Lumen.with(holder.imageView.context)
            .load(images[position]) {
                roundedCorners(12f)
            }
            .into(holder.imageView)
    }
    
    override fun getItemCount() = images.size
    
    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        // 自动取消加载任务，避免内存泄漏
        holder.cancelLumenLoad()
    }
}

