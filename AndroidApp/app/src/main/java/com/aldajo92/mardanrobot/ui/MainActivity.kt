package com.aldajo92.mardanrobot.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.aldajo92.mardanrobot.JoyStick
import com.aldajo92.mardanrobot.presentation.MainViewModel
import com.github.niqdev.mjpeg.DisplayMode
import com.github.niqdev.mjpeg.MjpegInputStream
import com.github.niqdev.mjpeg.MjpegSurfaceView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val inputStreamState by viewModel.inputStreamFlow.observeAsState()
            BodyComposable(inputStreamState)
        }
    }

    @Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
    @Composable
    fun BodyComposable(
        inputStream: MjpegInputStream? = null
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            Row(
                Modifier
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp)
                    .weight(1f)
            ) {
                Card(
                    modifier = Modifier.fillMaxHeight(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    VideoStreamView(
                        modifier = Modifier.width(300.dp),
                        inputStream = inputStream
                    )
                }
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    (0..2).map {
                        item {
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(60.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) { }
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                JoyStick(
                    modifier = Modifier
                        .padding(30.dp)
                        .align(Alignment.BottomStart),
                    size = 100.dp,
                    dotSize = 30.dp,
                    backgroundComposable = {
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                    },
                    dotComposable = {
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Black)
                        )
                    }
                ) { x, y ->
                    Log.d("JoyStick1", "$x, $y")
                }
                JoyStick(
                    modifier = Modifier
                        .padding(30.dp)
                        .align(Alignment.BottomEnd),
                    size = 100.dp,
                    dotSize = 30.dp,
                    backgroundComposable = {
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Red)
                        )
                    },
                    dotComposable = {
                        Spacer(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.Black)
                        )
                    }
                ) { x, y ->
                    Log.d("JoyStick2", "$x, $y")
                }
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = { viewModel.startConnection() }) {
                    Text(text = "Connect")

                }
            }
        }
    }


    @Preview
    @Composable
    fun VideoStreamView(
        modifier: Modifier = Modifier,
        inputStream: MjpegInputStream? = null
    ) {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                MjpegSurfaceView(context, null).apply {
                    if (inputStream != null) setSource(inputStream)
                    setDisplayMode(DisplayMode.BEST_FIT)
                    showFps(true)
                }
            },
            update = { view ->
                with(view) {
                    if (inputStream != null) setSource(inputStream) else stopPlayback()
                }
            }
        )
    }

}