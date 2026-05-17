package com.streaming.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun generateThumbnail(videoFile: File, thumbFile: File): Boolean = withContext(Dispatchers.IO){
    try {
        val process = ProcessBuilder(
            "ffmpeg", "-i", videoFile.absolutePath,
            "-ss", "00:01:00.000",
            "-vframes", "1",
            "-q:v", "2",
            thumbFile.absolutePath
        )
            .redirectErrorStream(true)
            .start()

        val exitCode = process.waitFor()

        return@withContext exitCode == 0 && thumbFile.exists()
    } catch (e: Exception){
        e.printStackTrace()
        return@withContext false
    }
}