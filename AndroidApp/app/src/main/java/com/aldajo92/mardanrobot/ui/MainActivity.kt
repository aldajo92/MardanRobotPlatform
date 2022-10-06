package com.aldajo92.mardanrobot.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.aldajo92.mardanrobot.presentation.MainViewModel
import com.aldajo92.mardanrobot.ui.components.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val inputStreamState by viewModel.inputStreamFlow.observeAsState()
            BodyContent(
                inputStream = inputStreamState,
                xyWrapper = xyWrapper,
                menuClicked = viewModel::startConnection,
                settingsClicked = {
                    startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                },
                joystickEvent = {
                    viewModel.setCurrentJoystickState(it)
                    xyWrapper.addEntry(it[0].valueY, 0)
                    xyWrapper.addEntry(it[1].valueY, 1)
                }
            )
        }
        hideSystemUI()
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 360)
@Composable
fun BodyContent(
    inputStream: MjpegInputStream? = null,
    xyWrapper: MultiXYWrapper? = null,
    menuClicked: () -> Unit = {},
    settingsClicked: () -> Unit = {},
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
                xyWrapper = xyWrapper
            )
        },
        bottomContent = {
            BottomContent(
                modifier = Modifier
                    .fillMaxWidth(),
                menuClicked = menuClicked,
                settingsClicked = settingsClicked,
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

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 200)
@Composable
fun TopContent(
    modifier: Modifier = Modifier,
    inputStream: MjpegInputStream? = null,
    xyWrapper: MultiXYWrapper? = null
) {
    Row(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            if (inputStream != null) VideoStreamView(
                inputStream = inputStream
            )
        }
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                ChartCard(
                    modifier = Modifier
                        .height(180.dp),
                    xyWrapper
                )
            }
        }
    }
}

@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 720, heightDp = 160)
@Composable
fun BottomContent(
    modifier: Modifier = Modifier,
    menuClicked: () -> Unit = {},
    settingsClicked: () -> Unit = {},
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
            settingsClicked = settingsClicked
        )
    }
}

@Preview
@Composable
fun MenuSection(
    modifier: Modifier = Modifier,
    menuClicked: () -> Unit = {},
    settingsClicked: () -> Unit = {},
) {
    Row(modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MenuButton(modifier = Modifier.fillMaxHeight(), componentClicked = menuClicked)
        SettingsButton(componentClicked = settingsClicked)
        SimpleCircularButton(Modifier.fillMaxHeight())
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

