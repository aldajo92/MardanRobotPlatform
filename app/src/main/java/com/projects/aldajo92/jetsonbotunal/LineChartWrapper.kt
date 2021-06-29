package com.projects.aldajo92.jetsonbotunal

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlin.collections.ArrayList

class LineChartWrapper(
    private val lineChart: LineChart
) {

    fun configureLineChart() {
        initLinearData()
        initDataSet()
    }

    private var linearEntryList: MutableList<Entry>? = null
    private var set1: LineDataSet =  LineDataSet(linearEntryList, "DataSet")

    private fun initDataSet() {
        set1.setDrawIcons(false)
        set1.lineWidth = 3f
        set1.setDrawCircleHole(false)
        set1.setDrawValues(false)
        set1.valueTextSize = 9f
        set1.setDrawFilled(false)
        set1.formLineWidth = 1f
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.formSize = 15f
        set1.fillColor = Color.BLACK
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        lineChart.data = LineData(dataSets)
    }

    private fun initLinearData() {
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setPinchZoom(false)
        lineChart.isScaleXEnabled = false
        lineChart.isScaleYEnabled = false
        val xAxis: XAxis = lineChart.xAxis
        xAxis.enableGridDashedLine(10f, 10f, 0f)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        val axisLeft: YAxis = lineChart.axisLeft
        axisLeft.removeAllLimitLines()
        axisLeft.setDrawZeroLine(false)
        axisLeft.setDrawLimitLinesBehindData(false)
        axisLeft.setDrawAxisLine(true)
        axisLeft.setDrawGridLines(true)
        axisLeft.setDrawLabels(true)
        axisLeft.isEnabled = true
        val axisRight: YAxis = lineChart.axisRight
        axisRight.setDrawAxisLine(false)
        axisRight.setDrawGridLines(false)
        axisRight.isEnabled = false
    }

    private var incomesData: List<Float>? = null

    private fun setEntries(incomesData: List<Float>?) {
        this.incomesData = incomesData
        if (incomesData != null && incomesData.isNotEmpty()) {
            linearEntryList = ArrayList()
            val entriesSize = incomesData.size
            for (index in 0 until entriesSize) {
                val entryNoteModel = incomesData[index]
                linearEntryList?.add(Entry(index.toFloat(), entryNoteModel))
            }
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1)
            lineChart.data = LineData(dataSets)
            if (lineChart.data != null && lineChart.data.dataSetCount > 0) {
                set1 = lineChart.data.getDataSetByIndex(0) as LineDataSet
                set1.values = linearEntryList
                lineChart.data.notifyDataChanged()
            }
        } else {
            lineChart.clear()
        }
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
    }

    var values: MutableList<Float> = ArrayList()
    fun addValue(value: Float){
        if (values.size > 50) {
            values.removeAt(0)
        }
        values.add(value)
        setEntries(values)
    }

    companion object {
        fun getInstance(lineChart: LineChart) = LineChartWrapper(lineChart).apply {
            configureLineChart()
        }
    }

}
