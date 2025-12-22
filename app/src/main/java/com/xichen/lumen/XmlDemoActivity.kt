package com.xichen.lumen

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.xichen.lumen.core.Lumen
import com.xichen.lumen.view.into
import com.xichen.lumen.view.load
import com.xichen.lumen.view.roundedCorners
import com.xichen.lumen.view.rotate
import com.xichen.lumen.view.blur

/**
 * XML 布局示例 Activity
 * 
 * 展示在传统 XML 布局中使用 Lumen 图片加载库的各种场景
 */
class XmlDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xml_demo)
        
        setupBasicUsage()
        setupTransformers()
        setupPlaceholderAndError()
    }
    
    /**
     * 基本使用示例
     */
    private fun setupBasicUsage() {
        // 基本加载
        Lumen.with(this)
            .load("https://image.liumingzhi.cn/file/4832421ab2eb68fa542e2.png")
            .into(findViewById<ImageView>(R.id.imageViewBasic))
        
        // 加载本地资源
        Lumen.with(this)
            .load(R.drawable.pikaqi)
            .into(findViewById<ImageView>(R.id.imageViewResource))
    }
    
    /**
     * 转换器示例
     */
    private fun setupTransformers() {
        // 圆角
        Lumen.with(this)
            .load("https://image.liumingzhi.cn/file/4832421ab2eb68fa542e2.png") {
                roundedCorners(20f)
            }
            .into(findViewById<ImageView>(R.id.imageViewRounded))
        
        // 旋转
        Lumen.with(this)
            .load("https://image.liumingzhi.cn/file/4832421ab2eb68fa542e2.png") {
                rotate(45f)
            }
            .into(findViewById<ImageView>(R.id.imageViewRotated))
        
        // 模糊
        Lumen.with(this)
            .load("https://image.liumingzhi.cn/file/4832421ab2eb68fa542e2.png") {
                blur(radius = 15f, scale = 0.5f)
            }
            .into(findViewById<ImageView>(R.id.imageViewBlurred))
        
        // 链式转换：圆角 + 旋转
        Lumen.with(this)
            .load("https://image.liumingzhi.cn/file/4832421ab2eb68fa542e2.png") {
                roundedCorners(30f)
                rotate(90f)
            }
            .into(findViewById<ImageView>(R.id.imageViewChained))
    }
    
    /**
     * 占位图和错误处理示例
     */
    private fun setupPlaceholderAndError() {
        // 带占位图的加载
        Lumen.with(this)
            .load("https://image.liumingzhi.cn/file/4c26ab1b462e8dd701151.png") {
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_dialog_alert)
            }
            .into(findViewById<ImageView>(R.id.imageViewWithPlaceholder))
        
        // 错误 URL 测试（会显示错误图片）
        Lumen.with(this)
            .load("https://image.liumingzhi.cn/file/4c26ab1b462e8dd701150.png") {
                placeholder(android.R.drawable.ic_menu_gallery)
                error(android.R.drawable.ic_dialog_alert)
            }
            .into(findViewById<ImageView>(R.id.imageViewError))
    }
}

