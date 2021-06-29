package com.projects.aldajo92.jetsonbotunal

import android.os.Bundle
import android.view.InputDevice
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.lineaChart_entries
import java.util.Timer
import kotlin.math.abs


class MainActivity : AppCompatActivity(), SocketManager.SocketListener {

    private val lineChartWrapper by lazy {
        LineChartWrapper.getInstance(lineaChart_entries)
    }

    private val socketManager by lazy { SocketManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        socketManager.connect()
    }

    private fun sendMessage(message: Any) = socketManager.sendData(message)

    override fun onDestroy() {
        super.onDestroy()
        socketManager.disconnect()
    }

    override fun onDataReceived(robotVelocityEncoder: RobotVelocityEncoder) {
        lineChartWrapper.addValue(robotVelocityEncoder.velocityEncoder)
    }

    private var globalTimer: Timer? = null
    private val minMovement = 0.1f
    private var alreadyOnZero = true

    var ly = 0f
    var rx = 0f

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return if (event.source and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK && event.action == MotionEvent.ACTION_MOVE) {
            globalTimer?.cancel()
            val joyLy = event.getAxisValue(MotionEvent.AXIS_Y)
            val joyRx = event.getAxisValue(MotionEvent.AXIS_Z)

            ly = if (abs(joyLy) >= minMovement) -joyLy else 0f
            rx = if (abs(joyRx) >= minMovement) -joyRx else 0f

            val movementMessage = MoveRobotMessage(ly, rx)
            if (abs(joyLy) >= minMovement || abs(joyRx) >= minMovement) {
                sendMessage(movementMessage)
                alreadyOnZero = false
            } else if (abs(joyLy) < minMovement && abs(joyRx) < minMovement && !alreadyOnZero) {
                sendMessage(movementMessage)
                alreadyOnZero = true
            }

            true
        } else super.onGenericMotionEvent(event)
    }

}
