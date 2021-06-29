package com.projects.aldajo92.jetsonbotunal

data class RobotVelocityEncoder(
    val velocityEncoder: Float
)

data class RobotImu(
    val velocity: Float,
    val magnet: Float
)

data class MoveRobotMessage(
    val steering: Float,
    val throttle: Float
)
