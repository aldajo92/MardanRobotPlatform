package com.projects.aldajo92.jetsonbotunal.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.InputDevice
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.niqdev.mjpeg.DisplayMode
import com.github.niqdev.mjpeg.MjpegInputStream
import com.projects.aldajo92.jetsonbotunal.R
import com.projects.aldajo92.jetsonbotunal.api.SharedPreferencesManager
import com.projects.aldajo92.jetsonbotunal.databinding.ActivityMainBinding
import com.projects.aldajo92.jetsonbotunal.getVideoStreamingPath
import com.projects.aldajo92.jetsonbotunal.models.MoveRobotMessage
import com.projects.aldajo92.jetsonbotunal.ui.configuration.ConfigurationDialog
import com.projects.aldajo92.jetsonbotunal.ui.views.MultiXYWrapper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val realTimeWrapper by lazy {
        MultiXYWrapper.getInstance(
            binding.lineChartControl,
            listOf(
                ColorTemplate.getHoloBlue(),
                Color.rgb(244, 10, 10)
            )
        )
    }

    private var captureTimer: Timer? = null

    private val mainViewModel: MainViewModel by viewModels()

    private val sharedPreferencesManager by lazy {
        SharedPreferencesManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingSettingsButton.setOnClickListener {
            ConfigurationDialog().show(supportFragmentManager, "configuration")
        }

        binding.floatingButtonPlay.setOnClickListener { triggerCaptureTimer() }

        binding.floatingButtonSave.setOnClickListener {
            val date = SimpleDateFormat(
                "yyyyMMdd_hhmmss",
                Locale.getDefault()
            ).format(Calendar.getInstance().time)

            mainViewModel.saveAllImage(
                "${filesDir.path}/$date"
            )
        }

        binding.seekBarVelocity.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                maxValue = p1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

    }

    private fun triggerCaptureTimer() {
        captureTimer = if (captureTimer == null) {
            binding.floatingButtonPlay.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(this, R.color.custom_red)
            )
            mainViewModel.runCaptureTimer(
                sharedPreferencesManager.getStoredVideoSampleTime().toLong()
            )
        } else {
            binding.floatingButtonPlay.backgroundTintList = ColorStateList.valueOf(
                getThemeAccentColor(this)
            )
            captureTimer?.let { it.cancel(); null }
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            binding.videoView.stopPlayback()
            mainViewModel.disconnectSocket()
        } catch (e: Exception) {
            Log.e("ADJ", "camera not opened")
        }
    }

    override fun onResume() {
        super.onResume()
        startVideoStream()
        mainViewModel.connectSocket(sharedPreferencesManager.getSelectedBaseUrl() ?: "")
    }

    private fun runCommandTimer(value: Any): Timer = fixedRateTimer(
        "timer",
        true,
        0,
        200
    ) {
        mainViewModel.sendMessageBySocket(value)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.disconnectSocket()
        binding.videoView.stopPlayback()
    }

    private var globalTimer: Timer? = null
    private val minMovement = 0.1f
    private var alreadyOnZero = true

    var ly = 0f
    var rx = 0f

    var maxValue = 100

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return if (event.source and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK && event.action == MotionEvent.ACTION_MOVE) {
            globalTimer?.cancel()
            val joyLy = event.getAxisValue(MotionEvent.AXIS_Y)
            val joyRx = event.getAxisValue(MotionEvent.AXIS_Z)

            // TODO: Move all related logic above to view model
            ly = if (abs(joyLy) >= minMovement) (-joyLy * maxValue) / 100 else 0f
            rx = if (abs(joyRx) >= minMovement) -joyRx else 0f

            if (ly >= 0) {
                binding.progressbarYPos.progress = (ly * maxValue).toInt()
                binding.progressbarYNeg.progress = 0
            } else {
                binding.progressbarYPos.progress = 0
                binding.progressbarYNeg.progress = (-ly * maxValue).toInt()
            }

            if (-rx >= 0) {
                binding.progressbarXPos.progress = (-rx * 100).toInt()
                binding.progressbarXNeg.progress = 0
            } else {
                binding.progressbarXPos.progress = 0
                binding.progressbarXNeg.progress = (rx * 100).toInt()
            }

            val movementMessage = MoveRobotMessage(ly, rx)
            if (abs(joyLy) >= minMovement || abs(joyRx) >= minMovement) {
                mainViewModel.sendMessageBySocket(movementMessage)
                alreadyOnZero = false
            } else if (abs(joyLy) < minMovement && abs(joyRx) < minMovement && !alreadyOnZero) {
                mainViewModel.sendMessageBySocket(movementMessage)
                alreadyOnZero = true
            }

            realTimeWrapper.addEntries(listOf(ly, rx))

            true
        } else super.onGenericMotionEvent(event)
    }

    private fun startVideoStream() {
        sharedPreferencesManager.getSelectedBaseUrl()?.let {
            mainViewModel.getMJPEJObserver(it.getVideoStreamingPath())
                .subscribe({ inputStream: MjpegInputStream? ->
                    binding.videoView.setSource(inputStream)
                    binding.videoView.setDisplayMode(DisplayMode.BEST_FIT)
                    binding.videoView.showFps(true)
                }, {
                    Toast.makeText(
                        this, "error connecting to robot",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.videoView.stopPlayback()
                })
        }

        binding.videoView.setOnFrameCapturedListener(mainViewModel)
    }

    private fun getThemeAccentColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, value, true)
        return value.data
    }

}
