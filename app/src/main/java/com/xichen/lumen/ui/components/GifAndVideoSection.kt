package com.xichen.lumen.ui.components

import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.xichen.lumen.core.Lumen
import com.xichen.lumen.view.*

/**
 * GIF 和视频帧示例组件
 */
@Composable
fun GifAndVideoSection() {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "GIF 动画和视频帧示例",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            
            // GIF 动画示例
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "1. GIF 动画加载",
                    style = androidx.compose.material3.MaterialTheme.typography.titleSmall
                )
                
                AndroidView(
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            // 加载 GIF 动画（自动检测并播放）
                            Lumen.with(ctx)
                                .load("https://media.giphy.com/media/3o7aCTPPb4gEbFE7Ic/giphy.gif")
                                .into(this)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                
                Text(
                    text = "代码：\nLumen.with(context)\n    .load(\"https://...gif\")\n    .into(imageView)\n// 自动检测 GIF 并播放动画",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }
            
            Divider()
            
            // 视频帧示例（需要实际视频文件）
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "2. 视频帧提取",
                    style = androidx.compose.material3.MaterialTheme.typography.titleSmall
                )
                
                Text(
                    text = "从视频文件提取指定时间点的帧：",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    text = "代码示例：\n" +
                            "// 提取第一帧\n" +
                            "Lumen.with(context)\n" +
                            "    .loadVideo(videoFile)\n" +
                            "    .into(imageView)\n\n" +
                            "// 提取指定时间点（5秒）\n" +
                            "val timeUs = 5_000_000L // 5秒 = 5,000,000微秒\n" +
                            "Lumen.with(context)\n" +
                            "    .loadVideo(videoFile, timeUs)\n" +
                            "    .into(imageView)\n\n" +
                            "// 从 Uri 提取\n" +
                            "Lumen.with(context)\n" +
                            "    .loadVideo(videoUri, timeUs)\n" +
                            "    .into(imageView)",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }
            
            Divider()
            
            // 最佳实践说明
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "最佳实践",
                    style = androidx.compose.material3.MaterialTheme.typography.titleSmall
                )
                
                Text(
                    text = "• GIF 动画：\n" +
                            "  - API 28+ 完整支持动画播放\n" +
                            "  - API < 28 自动降级为静态图片（第一帧）\n" +
                            "  - 动画会自动启动，无需手动调用\n\n" +
                            "• 视频帧：\n" +
                            "  - 支持从 File 和 Uri 提取帧\n" +
                            "  - 可以指定任意时间点（微秒）\n" +
                            "  - 提取的帧支持所有转换器（圆角、模糊等）\n" +
                            "  - 支持内存缓存，提升性能\n\n" +
                            "• 性能优化：\n" +
                            "  - 视频帧提取在 IO 线程执行\n" +
                            "  - 结果自动缓存到内存\n" +
                            "  - 支持 RecyclerView 自动取消",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

