package com.aldajo92.mardanrobot.ui.components

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MultiXYWrapper(
    private val colorLines: List<Int> = listOf()
) {

    private var chart: LineChart? = null

    fun init(chart: LineChart) {
        this.chart = chart
        configureChart()
    }

    fun configureChart() {
        chart?.apply {
            description.isEnabled = false

            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setDrawGridBackground(false)

            setPinchZoom(false)
            isScaleXEnabled = false
            isScaleYEnabled = false

            // if disabled, scaling can be done on x- and y-axis separately
            setPinchZoom(true)

            val dataTmp = LineData()
            dataTmp.setValueTextColor(Color.WHITE)

            // add empty data
            data = dataTmp

            legend.isEnabled = false

            val xl: XAxis = xAxis
            xl.enableGridDashedLine(10f, 10f, 0f)
            xl.position = XAxis.XAxisPosition.BOTH_SIDED
            xl.setDrawLabels(false)
//            xl.setDrawAxisLine(false) // Used to remove top and bottom limits
            xl.granularity = 1f

            val leftAxis: YAxis = axisLeft
            leftAxis.textColor = Color.WHITE
            leftAxis.granularity = 0.1f
            leftAxis.setDrawLabels(false)
            leftAxis.setDrawAxisLine(false) // Used to remove top and bottom limits
            leftAxis.setDrawGridLines(true)

            val rightAxis: YAxis = axisRight
            rightAxis.isEnabled = true
            rightAxis.setDrawGridLines(false)
            rightAxis.setDrawAxisLine(false) // Used to remove top and bottom limits
            rightAxis.setDrawLabels(false)
        }

    }

    fun addEntry(y: Float, i: Int) {
        chart?.apply {
            val localData: LineData? = data
            if (localData != null) {
                var set = localData.getDataSetByIndex(i)
                if (set == null) {
                    set = createSet(colorLines[i])
                    localData.addDataSet(set)
                }
                localData.addEntry(Entry(set.entryCount.toFloat(), y), i)
                localData.notifyDataChanged()

                // let the chart know it's data has changed
                notifyDataSetChanged()

                // limit the number of visible entries
                setVisibleXRangeMaximum(50F)
                // setVisibleYRange(30, AxisDependency.LEFT);

                // move to the latest entry
                moveViewToX(localData.entryCount.toFloat())
            }
        }
    }

    private fun createSet(colorLine: Int): LineDataSet {
        val set = LineDataSet(null, "Data 1")
        set.axisDependency = AxisDependency.LEFT
        set.color = colorLine
        set.setCircleColor(colorLine)
        set.lineWidth = 2f
        set.setCircleColorHole(colorLine)
        set.circleRadius = 2f
        set.fillColor = colorLine
        set.highLightColor = Color.rgb(244, 117, 117)
        set.valueTextColor = Color.WHITE
        set.valueTextSize = 9f
        set.setDrawValues(false)
        return set
    }

//    companion object {
//        fun getInstance(chart: LineChart, colorLines: List<Int>) =
//            MultiXYWrapper(chart, colorLines).apply {
//                configureChart()
//            }
//    }

}
