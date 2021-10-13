package com.projects.aldajo92.jetsonbotunal.ui.graphs

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.utils.ColorTemplate
import com.projects.aldajo92.jetsonbotunal.R
import com.projects.aldajo92.jetsonbotunal.databinding.FragmentGraphsBinding
import com.projects.aldajo92.jetsonbotunal.ui.MainViewModel
import com.projects.aldajo92.jetsonbotunal.ui.views.SingleRealTimeWrapper

class GraphsFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var lineChartOutput: SingleRealTimeWrapper

    private lateinit var lineChartInput: SingleRealTimeWrapper

    private lateinit var binding: FragmentGraphsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentGraphsBinding.inflate(inflater, container, false)
        .let {
            binding = it
            it.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lineChartInput = SingleRealTimeWrapper.getInstance(
            binding.lineChartFirst,
            ColorTemplate.getHoloBlue(),
            requireContext().getString(R.string.input_pwm_data)
        )

        lineChartOutput =
            SingleRealTimeWrapper.getInstance(
                binding.lineChartSecond,
                Color.rgb(200, 200, 200),
                requireContext().getString(R.string.velocity_graph_measure)
            )

        mainViewModel.velocityLiveData.observe(viewLifecycleOwner, { velocityEncoder ->
            velocityEncoder?.let {
                lineChartOutput.addEntry(velocityEncoder.velocityEncoder)
                lineChartInput.addEntry(velocityEncoder.input)
            }
        })
    }
}
