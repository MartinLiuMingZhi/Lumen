# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep all public APIs
-keep class com.xichen.lumen.** { *; }
-keep interface com.xichen.lumen.** { *; }

# Keep Kotlin metadata
-keepclassmembers class * {
    @kotlin.jvm.JvmStatic <methods>;
}

