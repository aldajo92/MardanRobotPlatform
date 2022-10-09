package com.aldajo92.mardanrobot.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.aldajo92.mardanrobot.repositories.robot_message.RobotMessageRepository
import com.aldajo92.mardanrobot.repositories.video_stream.VideoRepository
import com.aldajo92.mardanrobot.ui.JoystickValues
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.lang.Math.random
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val robotMessageRepository: RobotMessageRepository
) : ViewModel() {

    private var joystickValuesArray = arrayOf(JoystickValues(), JoystickValues())
    private var clockJob: Job? = null

    //////////////////// Flow Section ////////////////////
    val inputStreamFlow = videoRepository.getStreamingImageFlow().asLiveData()

    //    val robotMessagesFlow = robotMessageRepository.messageFlow().asLiveData()
    private var messageLog = "value\n"
    val robotMessagesFlow = flow {
        while (true){
            messageLog += "${random()}\n"
            emit(messageLog)
            delay(1_000)
        }
    }.asLiveData()

    //////////////////// Live Data Section ////////////////////
    private var _joystickValuesLiveData = MutableLiveData<Array<JoystickValues>>()
    val joystickValuesLiveData: LiveData<Array<JoystickValues>> = _joystickValuesLiveData

    fun startConnection() {
        videoRepository.startConnection("http://192.168.4.1:8080/stream?topic=/camera/BGR/raw")
        robotMessageRepository.startConnection("http://192.168.4.1:8080")
    }

    fun setCurrentJoystickState(arrayOfJoystickValues: Array<JoystickValues>) {
        this.joystickValuesArray = arrayOfJoystickValues
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startClock() {
        if (clockJob == null) {
            clockJob = tickerFlow(0.2.seconds)
                .map { LocalDateTime.now() }
//            .distinctUntilChanged { old, new ->
//                old.minute == new.minute
//            }
                .onEach {
                    _joystickValuesLiveData.value = joystickValuesArray
                }
                .launchIn(viewModelScope)
        } else {
            clockJob?.cancel()
            clockJob = null
        }
    }

    private fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

}
