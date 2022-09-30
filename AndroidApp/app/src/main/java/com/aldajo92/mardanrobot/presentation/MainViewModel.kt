package com.aldajo92.mardanrobot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.aldajo92.mardanrobot.data_sources.image_stream.VideoRepository
import com.github.niqdev.mjpeg.MjpegInputStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {

    val inputStreamFlow = videoRepository.getStreamingImageFlow().asLiveData()

    fun startConnection(){
        videoRepository.startConnection("http://192.168.4.1:8080/stream?topic=/camera/BGR/raw")
    }


}