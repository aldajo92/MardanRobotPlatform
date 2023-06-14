package com.aldajo92.mardanrobot.framework

import com.aldajo92.mardanrobot.models.RobotVelocityEncoder

interface SocketListener {
    fun onDataReceived(robotVelocityEncoder: RobotVelocityEncoder)
}
