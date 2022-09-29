package com.aldajo92.mardanrobot.data_sources.image_stream

import com.github.niqdev.mjpeg.MjpegInputStream
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getStreamingImageFlow(): Flow<MjpegInputStream?>

    fun startConnection(urlPath: String)

    fun closeConnection()

}