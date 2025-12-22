# Lumen

<div align="center">

![Lumen Logo](https://img.shields.io/badge/Lumen-Image%20Loader-blue?style=for-the-badge)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?style=for-the-badge&logo=kotlin)
![Android](https://img.shields.io/badge/Android-API%2021+-green?style=for-the-badge&logo=android)
![License](https://img.shields.io/badge/License-Apache%202.0-yellow?style=for-the-badge)

**一个 Kotlin-first 的 Android 图片加载库，面向业务友好、AI 场景、列表场景**

[English](README.md) • [快速开始](#-快速开始) • [特性](#-特性) • [对比](#-与-glide--coil-对比) • [文档](#-文档)

</div>

---

## 🤔 Why（为什么要做）

在 Android 图片加载领域，虽然已有 Glide、Coil 等优秀库，但在实际业务开发中，我们遇到了以下痛点：

1. **状态不透明**：难以精确控制加载状态（Loading / Success / Error / Fallback），业务需要自定义 UI 时不够灵活
2. **链路黑盒**：加载链路不够透明，难以调试和定制（如加密图片、自定义解码等）
3. **RecyclerView 优化不足**：在列表场景中容易出现图片错乱、内存泄漏等问题
4. **Kotlin 特性利用不足**：现有库多为 Java 设计，未能充分利用 Kotlin 的 DSL、协程等特性
5. **AI 场景支持不足**：对于需要解密、自定义解码等 AI 相关场景支持不够友好

**Lumen 的定位**：不是另一个 Glide 克隆，而是为「真实业务 + AI 场景」设计的现代 Android 图片加载库。

---

## ✨ 特性

### 核心特性

- ✅ **状态可控**：清晰的加载状态（Loading / Success / Error / Fallback），支持自定义 UI
- ✅ **链路透明**：每一步都可插拔（Fetcher → Decryptor → Decoder → Transformer → Cache）
- ✅ **Kotlin-first**：充分利用 DSL、协程、Flow 等现代 Kotlin 特性
- ✅ **RecyclerView 优化**：自动取消复用 View 的加载任务，防止内存泄漏和图片错乱
- ✅ **图片转换**：圆角、旋转、裁剪、模糊等（直接作用于 Bitmap，而非 View）
- ✅ **多数据源**：支持 URL、File、Uri、Resource ID
- ✅ **Compose 支持**：原生 Jetpack Compose 组件和状态管理
- ✅ **内存缓存**：基于 LruCache 的自动内存缓存

### 技术亮点

- 🔄 **协程驱动**：基于 Kotlin Coroutines 和 Flow
- 🎭 **状态管理**：Sealed Class 表示加载状态
- 🧩 **模块化设计**：核心逻辑与 UI 分离（`lumen-core` 无 Android UI 依赖）
- 🛡️ **类型安全**：充分利用 Kotlin 类型系统

---

## 🚀 快速开始

### 1. 添加依赖

**简单方式（推荐）：** 只需添加一个依赖即可使用所有功能：

```kotlin
dependencies {
    implementation("com.xichen.lumen:lumen:1.0.0")
}
```

**模块化方式（可选）：** 如果只需要特定模块：

```kotlin
dependencies {
    implementation("com.xichen.lumen:lumen-core:1.0.0")      // 仅核心功能
    implementation("com.xichen.lumen:lumen-view:1.0.0")      // View 支持
    implementation("com.xichen.lumen:lumen-transform:1.0.0") // 转换功能
}
```

### 2. 添加权限

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 3. 使用（10 行代码）

```kotlin
Lumen.with(context)
    .load("https://example.com/image.jpg") {
        placeholder(R.drawable.placeholder)
        error(R.drawable.error)
        roundedCorners(12f)
    }
    .into(imageView)
```

**就这么简单！** 🎉

---

## 📊 与 Glide / Coil 对比

| 特性 | Lumen | Glide | Coil |
|------|-------|-------|------|
| **Kotlin-first** | ✅ 原生 Kotlin，充分利用 DSL、协程 | ❌ Java 设计，Kotlin 扩展有限 | ✅ Kotlin-first |
| **状态透明** | ✅ Sealed Class，状态清晰可控 | ⚠️ 状态不够透明 | ⚠️ 状态不够透明 |
| **链路可插拔** | ✅ 每一步都可自定义 | ⚠️ 部分可定制 | ⚠️ 部分可定制 |
| **RecyclerView 优化** | ✅ 自动取消，防止错乱 | ✅ 支持 | ✅ 支持 |
| **图片转换作用于 Bitmap** | ✅ 直接作用于 Bitmap | ❌ 作用于 View | ✅ 作用于 Bitmap |
| **Compose 支持** | ✅ 原生支持 | ⚠️ 需要适配 | ✅ 原生支持 |
| **加密图片支持** | ✅ 内置 Decryptor 接口 | ❌ 需要自定义 | ❌ 需要自定义 |
| **学习曲线** | ⭐⭐ 简单直观 | ⭐⭐⭐ 功能复杂 | ⭐⭐ 相对简单 |
| **包体积** | 📦 小（模块化） | 📦📦 中等 | 📦 小 |
| **成熟度** | 🆕 新项目 | ✅ 非常成熟 | ✅ 成熟 |

### 选择建议

- **选择 Lumen**：需要状态可控、链路透明、AI 场景支持、Kotlin-first 体验
- **选择 Glide**：需要 GIF 支持、非常成熟的生态、大量第三方插件
- **选择 Coil**：需要轻量级、Compose 原生支持、现代 Kotlin API

---

## 🎯 适用场景

### ✅ 适用场景

1. **业务友好场景**
   - 需要精确控制加载状态（Loading / Success / Error / Fallback）
   - 需要自定义 UI 展示（如骨架屏、自定义错误 UI）
   - 需要清晰的错误处理和兜底机制

2. **AI 场景**
   - 加密图片加载（内置 Decryptor 接口）
   - 自定义解码逻辑
   - 图片预处理和后处理

3. **列表场景**
   - RecyclerView 中的图片加载
   - 需要防止图片错乱和内存泄漏
   - 需要自动取消复用 View 的加载任务

4. **Kotlin 项目**
   - 纯 Kotlin 项目
   - 使用 Jetpack Compose
   - 需要现代 Kotlin API（DSL、协程、Flow）

5. **图片转换场景**
   - 需要圆角、旋转、裁剪、模糊等转换
   - 需要转换直接作用于 Bitmap（而非 View）
   - 需要链式转换

### ❌ 不适用场景

1. **GIF / Video Frame**
   - 当前版本不支持 GIF 动画
   - 不支持视频帧提取

2. **复杂动画**
   - 不支持图片加载动画（如淡入淡出）
   - 不支持过渡动画

3. **自动跨进程缓存**
   - 当前版本仅支持内存缓存
   - 不支持自动磁盘缓存（可自行实现）

4. **Java 项目**
   - 虽然可以在 Java 中使用，但体验不如 Kotlin
   - 建议使用 Glide 或 Coil

5. **需要大量第三方插件**
   - 生态相对较新，第三方插件较少
   - 如需丰富生态，建议使用 Glide

---

## 📝 使用示例

### 基础用法

```kotlin
// 最简单的用法
Lumen.with(context)
    .load("https://example.com/image.jpg")
    .into(imageView)

// 带占位图和错误处理
Lumen.with(context)
    .load("https://example.com/image.jpg") {
        placeholder(R.drawable.placeholder)
        error(R.drawable.error)
    }
    .into(imageView)
```

### 图片转换

```kotlin
// 圆角
Lumen.with(context)
    .load("https://example.com/image.jpg") {
        roundedCorners(20f)
    }
    .into(imageView)

// 链式转换
Lumen.with(context)
    .load("https://example.com/image.jpg") {
        roundedCorners(30f)
        rotate(90f)
        blur(radius = 15f)
    }
    .into(imageView)
```

### Jetpack Compose

```kotlin
import com.xichen.lumen.view.compose.LumenImage

@Composable
fun ImageScreen() {
    LumenImage(
        url = "https://example.com/image.jpg",
        modifier = Modifier.size(200.dp),
        contentDescription = "示例图片",
        block = {
            placeholder(R.drawable.placeholder)
            roundedCorners(20f)
        }
    )
}
```

### RecyclerView 优化

```kotlin
class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Lumen 自动处理取消逻辑
        Lumen.with(holder.itemView.context)
            .load(images[position]) {
                roundedCorners(12f)
            }
            .into(holder.imageView)
    }
    
    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        // 可选：手动取消（Lumen 已自动处理）
        holder.itemView.cancelLumenLoad()
    }
}
```

### 高级用法：自定义 Decryptor

```kotlin
class CustomDecryptor : ImageDecryptor {
    override suspend fun decrypt(data: ByteArray): ByteArray {
        // 自定义解密逻辑
        return decryptedData
    }
}

Lumen.with(context)
    .load("https://example.com/encrypted-image.jpg") {
        decryptor(CustomDecryptor())
    }
    .into(imageView)
```

---

## 🏗️ 架构设计

### 核心加载链路

```
ImageRequest
   ↓
Fetcher（Network / File / Uri / Resource）
   ↓
Decryptor（可选）
   ↓
Decoder（BitmapFactory）
   ↓
Transformer（可选：圆角、旋转、裁剪、模糊等）
   ↓
Cache（Memory Cache）
   ↓
Target（ImageView / Compose / Custom）
```

**核心原则：每一步都可插拔**

### 模块结构

```
Lumen/
 ├── lumen-core        // 核心加载逻辑（无 Android UI 依赖）
 ├── lumen-view        // ImageView / ViewTarget / Compose 支持
 ├── lumen-transform   // 图片转换器（圆角、旋转、裁剪、模糊）
 └── sample-app        // 示例工程
```

### 状态模型

```kotlin
sealed class ImageState {
    object Loading : ImageState()
    data class Success(val bitmap: Bitmap) : ImageState()
    data class Error(val throwable: Throwable) : ImageState()
    object Fallback : ImageState()
}
```

---

## 📚 文档

### API 文档

- [核心 API](docs/api-core.md)
- [View API](docs/api-view.md)
- [Compose API](docs/api-compose.md)
- [Transform API](docs/api-transform.md)

### 更多示例

查看 [sample-app](app/) 模块获取完整示例代码。

---

## 🤝 贡献

我们欢迎所有形式的贡献！

### 如何贡献

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 开发指南

1. 遵循 Kotlin 编码规范
2. 添加必要的单元测试
3. 更新相关文档
4. 确保所有测试通过

---

## 📄 许可证

本项目采用 Apache License 2.0 许可证。详情请参阅 [LICENSE](LICENSE) 文件。

---

## 🙏 致谢

感谢所有为 Lumen 做出贡献的开发者！

特别感谢 Glide 和 Coil 项目，它们为 Android 图片加载领域做出了巨大贡献。

---

## 📞 联系方式

- **Issues**: [GitHub Issues](https://github.com/your-username/lumen/issues)
- **Email**: your-email@example.com

---

<div align="center">

**如果这个项目对你有帮助，请给一个 ⭐ Star！**

Made with ❤️ by Lumen Team

</div>

