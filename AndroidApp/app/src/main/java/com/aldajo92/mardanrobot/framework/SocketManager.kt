package com.aldajo92.mardanrobot.framework

import android.util.Log
import com.google.gson.Gson
import com.aldajo92.mardanrobot.models.RobotVelocityEncoder
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class SocketManager(
    private val socketPath: String,
    private val socketListener: SocketListener
) {

    lateinit var mSocket: Socket
    private val objectGSon by lazy { Gson() }

    fun connect() {
        try {
            mSocket = IO.socket(socketPath)
            Log.d("success", mSocket.id())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }

        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT) {
            Log.i(this::class.java.name, "connected")
        }
        mSocket.on(ROBOT_MESSAGE, robotMessage)
    }

    private val robotMessage = Emitter.Listener {
        val robotMessage = objectGSon.fromJson(it[0].toString(), RobotVelocityEncoder::class.java)
        socketListener.onDataReceived(robotMessage)
    }

    fun sendData(channel: String, src: Any) {
        val jsonData = objectGSon.toJson(src)
        mSocket.emit(channel, jsonData)
    }

    fun disconnect() {
        mSocket.disconnect()
    }

}

const val ROBOT_MESSAGE = "robot-message"
const val ROBOT_COMMAND = "robot-command"
