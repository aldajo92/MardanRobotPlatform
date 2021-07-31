package com.projects.aldajo92.jetsonbotunal.models

data class RobotVelocityEncoder(
    val velocityEncoder: Float,
    val direction: Float,
    val input: Float
)

data class MoveRobotMessage(
    val steering: Float,
    val throttle: Float
)
