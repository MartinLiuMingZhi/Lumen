# Android 开源图片加载库 —— 完整开发流程文档

> 本文档用于指导从 **0 到 1** 开发一个 **可开源、可维护、可扩展** 的 Android 图片加载库。
> 目标是：**不是 Demo，而是真正能被业务使用的库**。

---

## 一、项目目标与定位

### 1.1 项目定位

这是一个：
- Kotlin-first 的图片加载库
- 面向 **业务友好 / AI 场景 / 列表场景**
- 强调 **状态可控、兜底清晰、链路透明**

### 1.2 非目标（第一阶段不做）

- GIF / Video Frame
- Compose 专属适配
- 复杂动画
- 自动跨进程缓存

---

## 二、整体架构设计

### 2.1 核心加载链路

```
ImageRequest
   ↓
Fetcher（Network / File / Resource）
   ↓
Decryptor（可选）
   ↓
Decoder（BitmapFactory）
   ↓
Transformer（可选）
   ↓
Cache（Memory / Disk）
   ↓
Target（ImageView / Custom View）
```

> 核心原则：**每一步都可插拔**

---

## 三、模块划分（推荐）

```
Lumen/
 ├── lumen-core        // 核心加载逻辑（无 Android UI 依赖）
 ├── lumen-view        // ImageView / ViewTarget
 ├── lumen-decrypt    // 解密接口 + 示例实现（可选）
 ├── lumen-transform  // 圆角 / 裁剪 / 模糊
 ├── sample-app              // 示例工程
 └── README.md
```

---

## 四、核心模块设计说明

### 4.1 ImageRequest

职责：**描述一次图片加载请求**

包含：
- data（url / file / resId）
- placeholder / error
- decryptor（可选）
- transformers（可选）
- priority

原则：
> ImageRequest 是 immutable 的

---

### 4.2 ImageLoader

职责：
- 调度加载流程
- 管理协程生命周期
- 缓存协调

关键点：
- ApplicationContext
- IO Dispatcher
- 可取消任务（RecyclerView 安全）

---

### 4.3 Fetcher

职责：
- 获取原始数据（ByteArray / InputStream）

接口示例：

```kotlin
interface Fetcher {
    suspend fun fetch(): ByteArray
    val key: String
}
```

---

### 4.4 Decryptor（可选）

职责：
- 对图片字节数据进行解密

设计原则：
- 核心库不关心算法
- 不落明文磁盘

接口示例：

```kotlin
interface ImageDecryptor {
    fun decrypt(input: ByteArray): ByteArray
    val key: String
}
```

---

### 4.5 Decoder

职责：
- 将 ByteArray 解码为 Bitmap

第一阶段：
- BitmapFactory.decodeByteArray

---

### 4.6 Transformer

职责：
- Bitmap → Bitmap

接口示例：

```kotlin
interface BitmapTransformer {
    fun transform(source: Bitmap): Bitmap
    val key: String
}
```

---

### 4.7 Cache

#### 内存缓存（第一阶段必须）

- LruCache<String, Bitmap>
- key = data + decrypt + transform

#### 磁盘缓存（第二阶段）

- DiskLruCache
- 可配置

---

### 4.8 Target

职责：
- 将结果展示到 View

第一阶段：
- ImageViewTarget

---

## 五、加载状态模型（强烈推荐）

```kotlin
sealed class ImageState {
    object Loading : ImageState()
    data class Success(val bitmap: Bitmap) : ImageState()
    object Error : ImageState()
    object Fallback : ImageState()
}
```

用途：
- AI UI
- 多状态渲染
- 调试

---

## 六、失败兜底设计（差异化能力）

### 6.1 Drawable 兜底

```kotlin
error(R.drawable.error)
```

### 6.2 View 兜底（推荐）

```kotlin
fallback {
    layout(R.layout.image_fallback)
}
```

原则：
> 兜底是 UI，而不是图片

---

## 七、RecyclerView 场景规范

必须支持：
- ViewHolder 复用安全
- 自动取消旧任务
- 占位图立即显示

实现手段：
- Job 绑定 View tag
- onViewRecycled 主动 cancel

---

## 八、开发阶段划分（建议严格遵循）

### Stage 1：MVP
- 网络加载
- 内存缓存
- ImageView 支持

### Stage 2：可用性
- Transformer
- 状态模型
- RecyclerView 优化

### Stage 3：差异化
- 解密图片
- View 兜底
- 优先级

### Stage 4：工程化
- 模块拆分
- Sample App
- README

---

## 九、README 编写清单（开源关键）

必须包含：
- Why（为什么要做）
- Feature List
- Quick Start（10 行以内）
- 对比 Glide / Coil
- 适用 & 不适用场景

//使用模式
Lumen.with(context)
    .load(url) {
        placeholder(R.drawable.xxx)
        error(R.drawable.error)
        roundedCorners(12.dp)
        fallback {
            color(Color.Gray)
            icon(R.drawable.ic_error)
        }
    }
    .into(imageView)


---

## 十、开源质量自检表

- [ ] API 是否稳定、克制
- [ ] 是否有 Sample
- [ ] 是否避免业务强耦合
- [ ] 是否能被 RecyclerView 安全使用
- [ ] 是否有清晰失败兜底

---

## 十一、长期演进方向（可选）

- Compose Target
- Progressive Image
- 调试面板（Image Inspector）
- AI 图片流支持

---

> **一句话总结**：
> 这是一个为「真实业务 + AI 场景」设计的现代 Android 图片加载库，而不是另一个 Glide 克隆。

