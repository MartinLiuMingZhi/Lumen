package com.xichen.lumen.core

import android.graphics.Bitmap
import android.util.LruCache

/**
 * Lumen 内存缓存管理器
 * 
 * 使用 LruCache 实现内存缓存，自动管理 Bitmap 内存占用
 */
class MemoryCache(
    maxSize: Int = calculateDefaultMaxSize()
) {
    private val cache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(maxSize) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            // 计算 Bitmap 占用的内存大小（字节）
            return value.byteCount
        }
    }

    /**
     * 获取缓存的 Bitmap
     */
    fun get(key: String): Bitmap? {
        return cache.get(key)
    }

    /**
     * 存储 Bitmap 到缓存
     */
    fun put(key: String, bitmap: Bitmap) {
        cache.put(key, bitmap)
    }

    /**
     * 移除缓存
     */
    fun remove(key: String) {
        cache.remove(key)
    }

    /**
     * 清空缓存
     */
    fun clear() {
        cache.evictAll()
    }

    /**
     * 获取当前缓存大小
     */
    fun size(): Int {
        return cache.size()
    }

    /**
     * 获取最大缓存大小
     */
    fun maxSize(): Int {
        return cache.maxSize()
    }

    companion object {
        /**
         * 计算默认最大缓存大小
         * 使用可用内存的 1/8
         */
        private fun calculateDefaultMaxSize(): Int {
            val maxMemory = Runtime.getRuntime().maxMemory()
            return (maxMemory / 8).toInt()
        }
    }
}

