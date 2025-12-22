package com.xichen.lumen.ui.components

import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.xichen.lumen.core.Lumen
import com.xichen.lumen.transform.CropMode
import com.xichen.lumen.view.*

@Composable
fun TransformSection() {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "转换器示例",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            
            // 圆角
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AndroidView(
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            Lumen.with(ctx)
                                .load("https://image.liumingzhi.cn/file/4c26ab1b462e8dd701151.png") {
                                    roundedCorners(20f)
                                }
                                .into(this)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                )
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "圆角转换器",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "roundedCorners(20f)",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            // 旋转
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AndroidView(
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            Lumen.with(ctx)
                                .load("https://image.liumingzhi.cn/file/4c26ab1b462e8dd701151.png") {
                                    rotate(45f)
                                }
                                .into(this)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                )
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "旋转转换器",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "rotate(45f)",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            // 模糊
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AndroidView(
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            Lumen.with(ctx)
                                .load("https://image.liumingzhi.cn/file/4c26ab1b462e8dd701151.png") {
                                    blur(radius = 15f, scale = 0.5f)
                                }
                                .into(this)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(150.dp)
                )
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "模糊转换器",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "blur(radius = 15f)",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            // 链式转换
            AndroidView(
                factory = { ctx ->
                    ImageView(ctx).apply {
                        Lumen.with(ctx)
                            .load("https://image.liumingzhi.cn/file/4c26ab1b462e8dd701151.png") {
                                roundedCorners(30f)
                                rotate(90f)
                            }
                            .into(this)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            
            Text(
                text = "链式转换：圆角 + 旋转",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )
        }
    }
}

