package com.aldajo92.mardanrobot.presentation

import androidx.lifecycle.ViewModel
import com.aldajo92.mardanrobot.data_sources.image_stream.VideoRepository
import com.github.niqdev.mjpeg.MjpegInputStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {

    val inputStreamFlow = videoRepository.getStreamingImageFlow()

}
