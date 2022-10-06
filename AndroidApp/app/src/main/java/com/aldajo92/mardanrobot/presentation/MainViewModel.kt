package com.aldajo92.mardanrobot.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aldajo92.mardanrobot.data_sources.image_stream.VideoRepository
import com.aldajo92.mardanrobot.ui.JoystickValues
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {

    private var joystickValuesArray: Array<JoystickValues> =
        arrayOf(JoystickValues(), JoystickValues())

    private val clockJob = CoroutineScope(Dispatchers.IO).launch {

    }

    val inputStreamFlow = videoRepository.getStreamingImageFlow().asLiveData()

    fun startConnection() {
        videoRepository.startConnection("http://192.168.4.1:8080/stream?topic=/camera/BGR/raw")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setCurrentJoystickState(arrayOfJoystickValues: Array<JoystickValues>) {
        this.joystickValuesArray = arrayOfJoystickValues
        tickerFlow(5.seconds)
            .map { LocalDateTime.now() }
            .distinctUntilChanged { old, new ->
                old.minute == new.minute
            }
            .onEach {

            }
            .launchIn(viewModelScope)
    }

    fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

}
