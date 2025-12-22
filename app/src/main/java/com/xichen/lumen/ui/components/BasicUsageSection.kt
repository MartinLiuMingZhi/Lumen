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
import com.xichen.lumen.view.*

@Composable
fun BasicUsageSection() {
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "基本使用示例",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            
            // 基本加载
            AndroidView(
                factory = { ctx ->
                    ImageView(ctx).apply {
                        Lumen.with(ctx)
                            .load("https://image.liumingzhi.cn/file/4c26ab1b462e8dd701151.png")
                            .into(this)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            
            Text(
                text = "代码：\nLumen.with(context)\n    .load(\"https://...\")\n    .into(imageView)",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )
        }
    }
}

