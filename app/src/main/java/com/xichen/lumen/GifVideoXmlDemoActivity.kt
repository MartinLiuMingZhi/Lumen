package com.xichen.lumen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.xichen.lumen.core.Lumen
import com.xichen.lumen.core.VideoFrameExtractor
import com.xichen.lumen.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * GIF 和视频帧 XML 布局示例 Activity
 * 
 * 展示在传统 XML 布局中使用 Lumen 加载 GIF 和视频帧
 */
class GifVideoXmlDemoActivity : AppCompatActivity() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var selectedVideoUri: Uri? = null
    
    private val videoPickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedVideoUri = uri
            setupVideoFrames()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif_video_xml_demo)
        
        setupGifExample()
        setupVideoSelector()
    }
    
    /**
     * 设置 GIF 动画示例
     */
    private fun setupGifExample() {
        val imageViewGif = findViewById<ImageView>(R.id.imageViewGif)
        
        // 加载 GIF 动画（自动检测并播放）
        Lumen.with(this)
            .load("https://media.giphy.com/media/3o7aCTPPb4gEbFE7Ic/giphy.gif")
            .into(imageViewGif)
    }
    
    /**
     * 设置视频选择器
     */
    private fun setupVideoSelector() {
        val buttonSelectVideo = findViewById<Button>(R.id.buttonSelectVideo)
        buttonSelectVideo.setOnClickListener {
            videoPickerLauncher.launch("video/*")
        }
    }
    
    /**
     * 设置视频帧显示
     */
    private fun setupVideoFrames() {
        val videoUri = selectedVideoUri ?: return
        
        val textViewVideoInfo = findViewById<TextView>(R.id.textViewVideoInfo)
        val textViewFirstFrame = findViewById<TextView>(R.id.textViewFirstFrame)
        val imageViewFirstFrame = findViewById<ImageView>(R.id.imageViewVideoFirstFrame)
        val textViewMidFrame = findViewById<TextView>(R.id.textViewMidFrame)
        val imageViewMidFrame = findViewById<ImageView>(R.id.imageViewVideoMidFrame)
        val textViewVideoWithTransform = findViewById<TextView>(R.id.textViewVideoWithTransform)
        val imageViewVideoWithTransform = findViewById<ImageView>(R.id.imageViewVideoWithTransform)
        
        // 显示视频信息
        textViewVideoInfo.visibility = View.VISIBLE
        textViewFirstFrame.visibility = View.VISIBLE
        imageViewFirstFrame.visibility = View.VISIBLE
        
        // 获取视频时长
        scope.launch {
            val duration = VideoFrameExtractor.getDuration(this@GifVideoXmlDemoActivity, videoUri)
            if (duration > 0) {
                textViewVideoInfo.text = "视频时长: ${duration / 1000} 秒"
            }
            
            // 显示第一帧
            Lumen.with(this@GifVideoXmlDemoActivity)
                .loadVideo(videoUri)
                .into(imageViewFirstFrame)
            
            // 如果视频时长足够，显示中间帧
            if (duration > 2_000_000) {
                val midTimeUs = duration / 2
                textViewMidFrame.text = "中间帧（${midTimeUs / 1_000_000}秒）"
                textViewMidFrame.visibility = View.VISIBLE
                imageViewMidFrame.visibility = View.VISIBLE
                
                Lumen.with(this@GifVideoXmlDemoActivity)
                    .loadVideo(videoUri, midTimeUs)
                    .into(imageViewMidFrame)
            }
            
            // 显示视频帧 + 转换器
            textViewVideoWithTransform.visibility = View.VISIBLE
            imageViewVideoWithTransform.visibility = View.VISIBLE
            
            Lumen.with(this@GifVideoXmlDemoActivity)
                .loadVideo(videoUri) {
                    roundedCorners(16f)
                }
                .into(imageViewVideoWithTransform)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}

