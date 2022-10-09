package com.aldajo92.mardanrobot.repositories.robot_message

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RobotMessageRepositoryImpl : RobotMessageRepository {

    override fun startConnection(urlPath: String): Boolean = false

    override fun sendMessage(channel: String, messageObject: Any) = Unit

    override fun messageFlow(): Flow<String> = flow { emit("") }

    override fun endConnection(): Boolean = false

}
