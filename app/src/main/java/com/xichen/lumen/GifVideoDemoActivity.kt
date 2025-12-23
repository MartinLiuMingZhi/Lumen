package com.xichen.lumen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.xichen.lumen.core.Lumen
import com.xichen.lumen.core.VideoFrameExtractor
import com.xichen.lumen.ui.theme.LumenTheme
import com.xichen.lumen.view.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * GIF 和视频帧完整示例 Activity
 * 
 * 展示：
 * 1. GIF 动画加载和播放
 * 2. 视频帧提取（从文件）
 * 3. 视频帧提取（从 Uri）
 * 4. 不同时间点的帧提取
 * 5. 与转换器的结合使用
 */
class GifVideoDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LumenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GifVideoDemoScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GifVideoDemoScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // 在 Compose 中管理状态
    var selectedVideoUri by remember { mutableStateOf<Uri?>(null) }
    var videoDuration by remember { mutableStateOf<Long?>(null) }
    
    // 使用 rememberLauncherForActivityResult 在 Compose 中注册 Activity Result
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            // 重置视频时长，等待重新获取
            videoDuration = null
        }
    }
    
    val onSelectVideo: () -> Unit = {
        videoPickerLauncher.launch("video/*")
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GIF 和视频帧示例") },
                navigationIcon = {
                    IconButton(onClick = { 
                        (context as? ComponentActivity)?.finish()
                    }) {
                        Icon(Icons.Default.ArrowBack, "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // GIF 动画示例
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "GIF 动画示例",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        text = "自动检测 GIF 格式并播放动画（API 28+）",
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    AndroidView(
                        factory = { ctx ->
                            android.widget.ImageView(ctx).apply {
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
                        text = "代码：\nLumen.with(context)\n    .load(\"https://...gif\")\n    .into(imageView)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // 视频帧提取示例
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "视频帧提取示例",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Button(
                        onClick = onSelectVideo,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("选择视频文件")
                    }
                    
                    if (selectedVideoUri != null) {
                        // 获取视频时长
                        LaunchedEffect(selectedVideoUri) {
                            scope.launch {
                                videoDuration = VideoFrameExtractor.getDuration(
                                    context,
                                    selectedVideoUri!!
                                )
                            }
                        }
                        
                        if (videoDuration != null && videoDuration!! > 0) {
                            Text(
                                text = "视频时长: ${videoDuration!! / 1000} 秒",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        // 显示第一帧
                        Text(
                            text = "第一帧（0秒）",
                            style = MaterialTheme.typography.titleSmall
                        )
                        
                        // 使用 key() 确保当 selectedVideoUri 改变时重新创建 ImageView
                        key(selectedVideoUri) {
                            AndroidView(
                                factory = { ctx ->
                                    android.widget.ImageView(ctx).apply {
                                        // 在 factory 中初始化加载
                                        Lumen.with(ctx)
                                            .loadVideo(selectedVideoUri!!)
                                            .into(this)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                        
                        // 显示中间帧（如果视频时长足够）
                        if (videoDuration != null && videoDuration!! > 2_000_000) {
                            val midTimeUs = videoDuration!! / 2
                            
                            Text(
                                text = "中间帧（${midTimeUs / 1_000_000}秒）",
                                style = MaterialTheme.typography.titleSmall
                            )
                            
                            key("$selectedVideoUri-$midTimeUs") {
                                AndroidView(
                                    factory = { ctx ->
                                        android.widget.ImageView(ctx).apply {
                                            Lumen.with(ctx)
                                                .loadVideo(selectedVideoUri!!, midTimeUs)
                                                .into(this)
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            }
                        }
                        
                        Text(
                            text = "代码：\n// 第一帧\nLumen.with(context)\n    .loadVideo(videoUri)\n    .into(imageView)\n\n" +
                                    "// 指定时间点\nval timeUs = 5_000_000L // 5秒\nLumen.with(context)\n    .loadVideo(videoUri, timeUs)\n    .into(imageView)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "请选择一个视频文件",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // 视频帧 + 转换器示例
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "视频帧 + 转换器",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        text = "视频帧提取后可以应用所有转换器（圆角、模糊等）",
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    if (selectedVideoUri != null) {
                        key("$selectedVideoUri-rounded") {
                            AndroidView(
                                factory = { ctx ->
                                    android.widget.ImageView(ctx).apply {
                                        Lumen.with(ctx)
                                            .loadVideo(selectedVideoUri!!) {
                                                roundedCorners(16f)
                                            }
                                            .into(this)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }
                        
                        Text(
                            text = "代码：\nLumen.with(context)\n    .loadVideo(videoUri) {\n        roundedCorners(16f)\n    }\n    .into(imageView)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "请先选择视频文件",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // 最佳实践说明
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "最佳实践",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        text = "1. GIF 动画：\n" +
                                "   • API 28+ 完整支持动画播放\n" +
                                "   • API < 28 自动降级为静态图片\n" +
                                "   • 动画自动启动，无需手动调用\n\n" +
                                "2. 视频帧提取：\n" +
                                "   • 支持 File 和 Uri 两种方式\n" +
                                "   • 时间单位：微秒（1秒 = 1,000,000微秒）\n" +
                                "   • 提取的帧支持所有转换器\n" +
                                "   • 结果自动缓存，提升性能\n\n" +
                                "3. 性能优化：\n" +
                                "   • 视频帧提取在 IO 线程执行\n" +
                                "   • 支持 RecyclerView 自动取消\n" +
                                "   • 内存缓存减少重复提取",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

