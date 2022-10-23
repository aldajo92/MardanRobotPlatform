package com.aldajo92.mardanrobot.repositories.robot_message

import com.aldajo92.mardanrobot.models.RobotVelocityEncoder
import kotlinx.coroutines.flow.Flow

interface RobotMessageRepository {

    fun startConnection(urlPath: String): Boolean

    fun sendMessage(channel: String, messageObject: Any)

    fun messageFlow(): Flow<RobotVelocityEncoder?>

    fun endConnection(): Boolean

}
