plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.xichen.lumen"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.xichen.lumen"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // Debug 配置
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

// 配置APK输出名称为 Lumen.apk
// 在构建任务完成后重命名 APK
afterEvaluate {
    tasks.named("assembleRelease") {
        doLast {
            val releaseDir = file("build/outputs/apk/release")
            releaseDir.listFiles()?.forEach { apkFile ->
                if (apkFile.name.endsWith(".apk") && apkFile.name != "Lumen.apk") {
                    val targetApk = File(releaseDir, "Lumen.apk")
                    apkFile.renameTo(targetApk)
                    println("✅ Renamed APK to: ${targetApk.name}")
                }
            }
        }
    }

    tasks.named("assembleDebug") {
        doLast {
            val debugDir = file("build/outputs/apk/debug")
            debugDir.listFiles()?.forEach { apkFile ->
                if (apkFile.name.endsWith(".apk") && apkFile.name != "Lumen-debug.apk") {
                    val targetApk = File(debugDir, "Lumen-debug.apk")
                    apkFile.renameTo(targetApk)
                    println("✅ Renamed APK to: ${targetApk.name}")
                }
            }
        }
    }
}

dependencies {
    // Lumen - 聚合模块（包含所有子模块）
    implementation(project(":lumen"))
    
    // 或者单独使用子模块（可选）
    // implementation(project(":lumen-core"))
    // implementation(project(":lumen-view"))
    // implementation(project(":lumen-transform"))
    
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    
    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.activity.compose)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
}