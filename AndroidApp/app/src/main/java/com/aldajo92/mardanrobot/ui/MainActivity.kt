package com.aldajo92.mardanrobot.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aldajo92.mardanrobot._settings.SettingsActivity
import com.aldajo92.mardanrobot.presentation.MainViewModel
import com.aldajo92.mardanrobot.ui.components.ChartCard
import com.aldajo92.mardanrobot.ui.components.JoyStick
import com.aldajo92.mardanrobot.ui.components.MultiXYWrapper
import com.aldajo92.mardanrobot.ui.components.QuadLineChart
import com.aldajo92.mardanrobot.ui.components.VideoStreamView
import com.aldajo92.mardanrobot.ui.theme.hideSystemUI
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.niqdev.mjpeg.MjpegInputStream
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.sin

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val xyWrapper = MultiXYWrapper(
        listOf(
            ColorTemplate.getHoloBlue(),
            android.graphics.Color.rgb(244, 10, 10)
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.joystickValuesLiveData.observe(this) {
            it?.let {
                xyWrapper.addEntry(it[0].valueY, 0)
                xyWrapper.addEntry(it[1].valueY, 1)
            }
        }

        setContent {
            val inputStreamState by viewModel.inputStreamFlow.observeAsState()
            val robotMessagesText by viewModel.robotMessagesFlow.observeAsState()

            BodyContent(
                inputStream = inputStreamState,
                xyWrapper = xyWrapper,
                messageText = robotMessagesText.orEmpty(),
                menuClicked = viewModel::startConnection,
                settingsClicked = { openSettings() },
                joystickEvent = { viewModel.setCurrentJoystickState(it) },
                simpleButtonClicked = { viewModel.startClock() }
            )
        }
        hideSystemUI()
    }

    private fun openSettings() {
        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun BodyContent(
    inputStream: MjpegInputStream? = null,
    xyWrapper: MultiXYWrapper = MultiXYWrapper(listOf(0)),
    messageText: String = "",
    menuClicked: () -> Unit = {},
    settingsClicked: () -> Unit = {},
    simpleButtonClicked: () -> Unit = {},
    joystickEvent: (Array<JoystickValues>) -> Unit = {}
) {
    BodyComposable(
        topContent = {
            TopContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp),
                inputStream = inputStream,
                xyWrapper = xyWrapper,
                messageText = messageText
            )
        },
        bottomContent = {
            BottomContent(
                modifier = Modifier
                    .fillMaxWidth(),
                menuClicked = menuClicked,
                settingsClicked = settingsClicked,
                simpleButtonClicked = simpleButtonClicked,
                joystickEvent = joystickEvent
            )
        }
    )
}

@Composable
fun BodyComposable(
    topContent: @Composable BoxScope.() -> Unit = {},
    bottomContent: @Composable BoxScope.() -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            topContent()
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            bottomContent()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 200)
@Composable
fun TopContent(
    modifier: Modifier = Modifier,
    inputStream: MjpegInputStream? = null,
    xyWrapper: MultiXYWrapper = MultiXYWrapper(listOf(0)),
    messageText: String = ""
) {
    val listState = rememberLazyListState()
    Row(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (inputStream != null) VideoStreamView(inputStream = inputStream)

            LaunchedEffect(messageText){
                listState.animateScrollBy(100f)
            }
            LazyColumn(state = listState) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxSize(),
                        text = messageText,
                        color = LightGray
                    )
                }
            }
        }
        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ChartCard(
                modifier = Modifier
                    .weight(1f),
                xyWrapper
            )
        }
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 160)
@Composable
fun BottomContent(
    modifier: Modifier = Modifier,
    menuClicked: () -> Unit = {},
    settingsClicked: () -> Unit = {},
    simpleButtonClicked: () -> Unit = {},
    joystickEvent: (Array<JoystickValues>) -> Unit = {}
) {
    val joystickArray = remember { arrayOf(JoystickValues(), JoystickValues()) }

    Box(modifier = modifier) {
        CustomJoystick(
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.BottomStart)
        ) {
            joystickArray[0] = it
            joystickEvent(joystickArray)
        }
        CustomJoystick(
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.BottomEnd)
        ) {
            joystickArray[1] = it
            joystickEvent(joystickArray)
        }
        MenuSection(
            modifier = Modifier.align(Alignment.Center),
            settingsClicked = settingsClicked,
            simpleButtonClicked = simpleButtonClicked
        )
    }
}

@Preview
@Composable
fun MenuSection(
    modifier: Modifier = Modifier,
    menuClicked: () -> Unit = {},
    settingsClicked: () -> Unit = {},
    simpleButtonClicked: () -> Unit = {}
) {
    Row(modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MenuButton(modifier = Modifier.fillMaxHeight(), componentClicked = menuClicked)
        SettingsButton(componentClicked = settingsClicked)
        SimpleCircularButton(Modifier.fillMaxHeight(), componentClicked = simpleButtonClicked)
    }
}

@Preview
@Composable
fun MenuButton(modifier: Modifier = Modifier, componentClicked: () -> Unit = {}) {
    Card(
        modifier = modifier
            .clickable { componentClicked() },
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(4.dp, Color.White),
        backgroundColor = Color.Transparent,
    ) {
        Box {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 10.dp)
                    .padding(horizontal = 40.dp),
                text = "MENU",
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun SettingsButton(modifier: Modifier = Modifier, componentClicked: () -> Unit = {}) {
    Card(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Min)
            .clickable { componentClicked() },
        shape = CircleShape,
        border = BorderStroke(4.dp, Color.White),
        backgroundColor = Color.Transparent,
    ) {
        Icon(
            modifier = Modifier
                .padding(12.dp),
            imageVector = Icons.Filled.Settings,
            tint = Color.White,
            contentDescription = "MENU"
        )
    }
}

@Preview(widthDp = 50, heightDp = 50)
@Composable
fun SimpleCircularButton(modifier: Modifier = Modifier, componentClicked: () -> Unit = {}) {
    Box(
        modifier = modifier
            .clickable { componentClicked() }
            .aspectRatio(1f)
            .border(4.dp, Color.White, shape = CircleShape)
    )
}

@Preview(widthDp = 100, heightDp = 100)
@Composable
fun CustomJoystick(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    joystickEvent: (JoystickValues) -> Unit = {}
) {
    var positionX by remember { mutableStateOf(0f) }
    var positionY by remember { mutableStateOf(0f) }
    Box(modifier = modifier) {
        JoyStick(
            Modifier,
            size = size,
            dotSize = size / 4,
            backgroundComposable = {
                Spacer(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Canvas(
                    Modifier.fillMaxSize(),
                ) {
                    val canvasWidth = this@Canvas.size.width
                    val canvasHeight = this@Canvas.size.height

                    val canvasCenterWidth = canvasWidth / 2
                    val canvasCenterHeight = canvasHeight / 2

                    drawLine(
                        start = Offset(x = canvasCenterWidth + positionX, y = 0f),
                        end = Offset(x = canvasCenterWidth + positionX, y = canvasHeight),
                        color = LightGray,
                        strokeWidth = 5f
                    )
                    drawLine(
                        start = Offset(x = 0f, y = canvasCenterHeight + positionY),
                        end = Offset(x = canvasWidth, y = canvasCenterHeight + positionY),
                        color = LightGray,
                        strokeWidth = 5f
                    )
                }
            },
            dotComposable = {
                Spacer(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(LightGray)
                )
            }
        ) { x: Float, y: Float ->
            positionX = (x * (size.value + 35.dp.value)) // + (x * size.value / (2 * 4))
            positionY = -(y * (size.value + 35.dp.value)) // - (y * size.value / (2 * 4)))
            joystickEvent(JoystickValues(x, y))
        }
    }
}

@Preview
@Composable
fun RenderChartCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.Gray
    ) {
        QuadLineChart(
            Modifier
                .height(100.dp)
                .fillMaxWidth(),
            (1..50).map { Pair(it, 1 + sin(2 * Math.PI * it / 10)) }
        )
    }
}
