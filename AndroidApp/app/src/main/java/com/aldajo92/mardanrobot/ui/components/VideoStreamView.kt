package com.aldajo92.mardanrobot.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.github.niqdev.mjpeg.DisplayMode
import com.github.niqdev.mjpeg.MjpegInputStream
import com.github.niqdev.mjpeg.MjpegSurfaceView

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
                showFps(true)
            }
        },
        update = { view ->
            with(view) {
                if (inputStream != null) {
                    setSource(inputStream)
                    setDisplayMode(DisplayMode.BEST_FIT)
                } else stopPlayback()
            }
        }
    )
}
