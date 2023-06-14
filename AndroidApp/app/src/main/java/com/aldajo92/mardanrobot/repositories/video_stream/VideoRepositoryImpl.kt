package com.aldajo92.mardanrobot.repositories.video_stream

import com.github.niqdev.mjpeg.Mjpeg
import com.github.niqdev.mjpeg.MjpegInputStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import rx.Subscription
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val mjpeg: Mjpeg
) : VideoRepository {
    private var disposable : Subscription? = null

    private val _streamingImageFlow: MutableStateFlow<MjpegInputStream?> = MutableStateFlow(null)

    override fun getStreamingImageFlow(): Flow<MjpegInputStream?> = _streamingImageFlow

    override fun startConnection(urlPath: String) {
        disposable = mjpeg.open(urlPath, 100)
            .subscribe({ inputStream: MjpegInputStream? ->
                _streamingImageFlow.value = inputStream
            }, {
                _streamingImageFlow.value = null
            })
    }

    override fun closeConnection() {
        disposable?.unsubscribe()
        disposable = null
        _streamingImageFlow.value = null
    }
}
