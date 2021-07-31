package com.projects.aldajo92.jetsonbotunal.ui.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.projects.aldajo92.jetsonbotunal.databinding.FragmentDataListBinding
import com.projects.aldajo92.jetsonbotunal.ui.MainViewModel
import com.projects.aldajo92.jetsonbotunal.ui.data.adapter.DataListAdapter

class DataListFragment : Fragment() {

    lateinit var binding: FragmentDataListBinding

    private val dataListAdapter by lazy { DataListAdapter() }

    private val mainViewModel: MainViewModel by activityViewModels()

    private val layoutManager by lazy {
        GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDataListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewData.adapter = dataListAdapter
        binding.recyclerViewData.layoutManager = layoutManager

        mainViewModel.dataImageLiveData.observe(viewLifecycleOwner, { dataImageModel ->
            dataImageModel?.let {
                dataListAdapter.setItem(it)
                binding.recyclerViewData.smoothScrollToPosition(dataListAdapter.itemCount - 1)
            }
        })
    }
}
