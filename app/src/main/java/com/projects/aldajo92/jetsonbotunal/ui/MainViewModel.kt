package com.projects.aldajo92.jetsonbotunal.ui

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.niqdev.mjpeg.Mjpeg
import com.github.niqdev.mjpeg.MjpegInputStream
import com.github.niqdev.mjpeg.OnFrameCapturedListener
import com.opencsv.CSVWriter
import com.projects.aldajo92.jetsonbotunal.api.SocketManager
import com.projects.aldajo92.jetsonbotunal.models.RobotVelocityEncoder
import com.projects.aldajo92.jetsonbotunal.ui.data.adapter.DataImageModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import rx.Observable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.util.Date
import kotlin.concurrent.fixedRateTimer

class MainViewModel : ViewModel(), SocketManager.SocketListener, OnFrameCapturedListener {

    private val _velocityLiveData = MutableLiveData<RobotVelocityEncoder>()
    val velocityLiveData: LiveData<RobotVelocityEncoder> get() = _velocityLiveData

    private val _dataImageLiveData = MutableLiveData<DataImageModel>()
    val dataImageLiveData: LiveData<DataImageModel> get() = _dataImageLiveData

    private val _dataListLiveData = MutableLiveData<List<DataImageModel>>()
    val dataListLiveData: LiveData<List<DataImageModel>> get() = _dataListLiveData
    private val dataList = mutableListOf<DataImageModel>()

    private var bitmapFrame: Bitmap? = null

    private val mjpegView by lazy {
        Mjpeg.newInstance()
    }

    lateinit var socketManager: SocketManager

    fun connectSocket(urlPath: String) {
        if (!this::socketManager.isInitialized) {
            socketManager = SocketManager(this, urlPath)
            socketManager.connect()
        }
    }

    fun sendMessageBySocket(message: Any) = socketManager.sendData(message)

    fun disconnectSocket() {
        socketManager.disconnect()
    }

    fun getMJPEJObserver(urlPath: String): Observable<MjpegInputStream> =
        mjpegView.open(urlPath, 100)

    override fun onDataReceived(robotVelocityEncoder: RobotVelocityEncoder) {
        _velocityLiveData.postValue(robotVelocityEncoder)
    }

    override fun onFrameCaptured(bitmap: Bitmap) {
        this.bitmapFrame = bitmap
    }

    fun saveAllImage(path: String) {
        File(path).mkdir()

        // https://blog.mindorks.com/parallel-multiple-network-calls-using-kotlin-coroutines
        viewModelScope.launch {
            launch(IO) {
                dataList.forEach { dataImageModel ->
                    dataImageModel.bitmap?.let {
                        saveImage(
                            it,
                            "$path/image_${dataImageModel.timeStamp}.jpg"
                        )
                    }
                }
            }

            // https://zetcode.com/java/opencsv/
            launch(IO) {
                val list = dataList.map { dataImageModel ->
                    arrayListOf(
                        "image_${dataImageModel.timeStamp}.jpg",
                        dataImageModel.timeStamp.toString(),
                        dataImageModel.steering.toString(),
                        dataImageModel.throttle.toString()
                    ).toTypedArray()
                }
                saveCSVFile(
                    list,
                    path,
                    "metadata_${dataList.first().timeStamp}_${dataList.last().timeStamp}"
                )
            }
        }
    }

    fun runCaptureTimer(sampleTime: Long) = fixedRateTimer("timer", true, 0, sampleTime) {
        saveInstantImage()
    }

    private fun saveInstantImage() {
        val velocityValue = velocityLiveData.value?.velocityEncoder
        val directionValue = velocityLiveData.value?.direction

        val dataImageModel = DataImageModel(
            bitmapFrame,
            Date().time,
            velocityValue ?: 0f,
            directionValue ?: 0f
        )
        dataList.add(dataImageModel)

        _dataImageLiveData.postValue(dataImageModel)

//        _dataListLiveData.postValue(dataList.toList())
    }

    private fun saveImage(bitmap: Bitmap, filename: String) {
        try {
            FileOutputStream(filename).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun saveCSVFile(list: List<Array<String>>, path: String, filename: String) {
        val file = File(path, "$filename.csv")
        FileOutputStream(file).use { fos ->
            OutputStreamWriter(fos, StandardCharsets.UTF_8).use { osw ->
                CSVWriter(osw).use { writer ->
                    writer.writeAll(list)
                }
            }
        }
    }

}
