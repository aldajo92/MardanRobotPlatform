package com.aldajo92.mardanrobot.models

data class MoveRobotMessage(
    val steering: Float,
    val throttle: Float,
    val pan: Float,
    val tilt: Float
)
