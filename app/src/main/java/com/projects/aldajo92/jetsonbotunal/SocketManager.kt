package com.projects.aldajo92.jetsonbotunal

import android.util.Log
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter

class SocketManager(private val socketListener: SocketListener) {

    lateinit var mSocket: Socket
    private val objectGSon by lazy { Gson() }

    fun connect() {
        try {
            mSocket = IO.socket(DATA_SOCKET_PATH)
            Log.d("success", mSocket.id())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }

        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, connectedEvent)
        mSocket.on(ROBOT_MESSAGE, robotMessage)
    }

    private val connectedEvent = Emitter.Listener {
        Log.i("ADJGF_TAG", "connected")
    }

    private val robotMessage = Emitter.Listener {
        val robotMessage = objectGSon.fromJson(it[0].toString(), RobotVelocityEncoder::class.java)
        socketListener.onDataReceived(robotMessage)
    }

    fun sendData(src: Any) {
        val jsonData = objectGSon.toJson(src)
        mSocket.emit(ROBOT_COMMAND, jsonData)
    }

    fun disconnect() {
        mSocket.disconnect()
    }

    interface SocketListener {
        fun onDataReceived(robotVelocityEncoder : RobotVelocityEncoder)
    }

}
