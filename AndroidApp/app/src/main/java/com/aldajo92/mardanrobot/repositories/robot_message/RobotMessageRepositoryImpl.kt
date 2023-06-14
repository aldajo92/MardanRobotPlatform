package com.aldajo92.mardanrobot.repositories.robot_message

import com.aldajo92.mardanrobot.framework.SocketListener
import com.aldajo92.mardanrobot.framework.SocketManager
import com.aldajo92.mardanrobot.models.RobotVelocityEncoder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class RobotMessageRepositoryImpl : RobotMessageRepository, SocketListener {

    private var socketManager: SocketManager? = null

    private val messagesFlow = MutableStateFlow<RobotVelocityEncoder?>(null)

    override fun startConnection(urlPath: String): Boolean {
        socketManager = SocketManager(urlPath, this)
        socketManager?.connect()
        return true
    }

    override fun sendMessage(channel: String, messageObject: Any) {
        socketManager?.sendData(channel, messageObject)
    }

    override fun messageFlow(): Flow<RobotVelocityEncoder?> = messagesFlow

    override fun endConnection(): Boolean = socketManager?.let {
        it.disconnect()
        true
    } ?: false

    override fun onDataReceived(robotVelocityEncoder: RobotVelocityEncoder) {
        messagesFlow.value = robotVelocityEncoder
    }

}
