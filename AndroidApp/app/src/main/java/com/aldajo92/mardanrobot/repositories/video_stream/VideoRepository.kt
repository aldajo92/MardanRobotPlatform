package com.aldajo92.mardanrobot.repositories.video_stream

import com.github.niqdev.mjpeg.MjpegInputStream
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getStreamingImageFlow(): Flow<MjpegInputStream?>

    fun startConnection(urlPath: String)

    fun closeConnection()

}
