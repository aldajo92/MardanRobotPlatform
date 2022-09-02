package com.projects.aldajo92.jetsonbotunal.ui.virtual_controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.projects.aldajo92.jetsonbotunal.databinding.FragmentVirtualControllersBinding

class VirtualControllersFragment: Fragment() {

    private lateinit var binding: FragmentVirtualControllersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentVirtualControllersBinding.inflate(inflater, container, false)
        .let {
            binding = it
            it.root
        }



}