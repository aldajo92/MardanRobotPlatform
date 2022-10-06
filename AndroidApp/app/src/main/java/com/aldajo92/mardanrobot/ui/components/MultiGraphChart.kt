package com.aldajo92.mardanrobot.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart

@Preview
@Composable
fun ChartCard(modifier: Modifier = Modifier, xyWrapper: MultiXYWrapper? = null) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        backgroundColor = Color.Transparent
    ) {
        MultiGraphChart(modifier = Modifier.fillMaxSize(), xyWrapper)
    }
}

@Composable
fun MultiGraphChart(modifier: Modifier = Modifier, xyWrapper: MultiXYWrapper? = null) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val lineChart = LineChart(context)
            xyWrapper?.init(lineChart)
            lineChart
        },
        update = { view ->
            xyWrapper?.init(view)
        }
    )
}
