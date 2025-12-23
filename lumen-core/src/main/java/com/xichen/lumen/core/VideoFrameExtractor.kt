package com.xichen.lumen.core

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Lumen 视频帧提取器
 * 
 * 负责从视频文件中提取指定时间点的帧
 */
object VideoFrameExtractor {
    /**
     * 从视频文件提取帧
     * @param file 视频文件
     * @param timeUs 时间点（微秒），默认 0（第一帧）
     * @return 提取的 Bitmap 帧
     */
    suspend fun extractFrame(
        file: File,
        timeUs: Long = 0
    ): Bitmap = withContext(Dispatchers.IO) {
        if (!file.exists()) {
            throw IllegalStateException("Video file not found: ${file.absolutePath}")
        }

        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(file.absolutePath)
            val bitmap = retriever.getFrameAtTime(
                timeUs,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )

            bitmap ?: throw IllegalStateException("Failed to extract frame from video at time $timeUs")
        } catch (e: Exception) {
            android.util.Log.e("Lumen", "Failed to extract video frame", e)
            throw e
        } finally {
            retriever.release()
        }
    }

    /**
     * 从视频 Uri 提取帧
     * @param context Context
     * @param uri 视频 Uri
     * @param timeUs 时间点（微秒），默认 0（第一帧）
     * @return 提取的 Bitmap 帧
     */
    suspend fun extractFrame(
        context: Context,
        uri: Uri,
        timeUs: Long = 0
    ): Bitmap = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
            val bitmap = retriever.getFrameAtTime(
                timeUs,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )

            bitmap ?: throw IllegalStateException("Failed to extract frame from video at time $timeUs")
        } catch (e: Exception) {
            android.util.Log.e("Lumen", "Failed to extract video frame", e)
            throw e
        } finally {
            retriever.release()
        }
    }

    /**
     * 获取视频时长（微秒）
     * @param file 视频文件
     * @return 视频时长（微秒），如果获取失败返回 -1
     */
    suspend fun getDuration(file: File): Long = withContext(Dispatchers.IO) {
        if (!file.exists()) {
            return@withContext -1L
        }

        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(file.absolutePath)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            duration?.toLongOrNull() ?: -1L
        } catch (e: Exception) {
            android.util.Log.e("Lumen", "Failed to get video duration", e)
            -1L
        } finally {
            retriever.release()
        }
    }

    /**
     * 获取视频时长（微秒）
     * @param context Context
     * @param uri 视频 Uri
     * @return 视频时长（微秒），如果获取失败返回 -1
     */
    suspend fun getDuration(context: Context, uri: Uri): Long = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            duration?.toLongOrNull() ?: -1L
        } catch (e: Exception) {
            android.util.Log.e("Lumen", "Failed to get video duration", e)
            -1L
        } finally {
            retriever.release()
        }
    }
}

