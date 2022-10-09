package com.aldajo92.mardanrobot.repositories.robot_message

import kotlinx.coroutines.flow.Flow

interface RobotMessageRepository {

    fun startConnection(urlPath: String): Boolean

    fun sendMessage(channel: String, messageObject: Any)

    fun messageFlow(): Flow<String>

    fun endConnection(): Boolean

}
