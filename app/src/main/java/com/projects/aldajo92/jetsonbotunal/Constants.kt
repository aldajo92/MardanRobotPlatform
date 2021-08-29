package com.projects.aldajo92.jetsonbotunal

const val ROBOT_MESSAGE = "robot-message"
const val ROBOT_COMMAND = "robot-command"

const val KEY_URL = "aldajo92.URL"
const val KEY_URL_LOCAL_IP = "aldajo92.URL_LOCAL"
const val KEY_URL_REMOTE = "aldajo92.URL_REMOTE"
const val KEY_SAMPLE_TIME = "aldajo92.SAMPLE_TIME"

const val VALUE_URL_LOCAL_IP = "http://192.168.0.123:80"
const val VALUE_URL_REMOTE = "https://jetsonbotunal.ngrok.io"
const val SOCKET_PATH = ""
const val STREAMING_PATH = "/stream?topic=/camera/BGR/raw"

fun String.getSocketPath() = "$this$SOCKET_PATH"
fun String.getVideoStreamingPath() = "$this$STREAMING_PATH"

const val NUM_PAGES = 2
