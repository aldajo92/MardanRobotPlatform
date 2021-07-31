package com.projects.aldajo92.jetsonbotunal.ui.data.adapter

import android.graphics.Bitmap

data class DataImageModel(
    val bitmap: Bitmap?,
    val timeStamp: Long,
    val steering: Float,
    val throttle: Float
)
