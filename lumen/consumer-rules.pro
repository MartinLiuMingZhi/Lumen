# Keep all public APIs
-keep class com.xichen.lumen.** { *; }
-keep interface com.xichen.lumen.** { *; }

# Keep Kotlin metadata
-keepclassmembers class * {
    @kotlin.jvm.JvmStatic <methods>;
}

