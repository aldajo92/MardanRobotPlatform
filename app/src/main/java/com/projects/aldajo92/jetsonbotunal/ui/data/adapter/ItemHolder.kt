package com.projects.aldajo92.jetsonbotunal.ui.data.adapter

import androidx.recyclerview.widget.RecyclerView
import com.projects.aldajo92.jetsonbotunal.R
import com.projects.aldajo92.jetsonbotunal.databinding.ItemDataBinding

class ItemHolder(private val binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var dataImageModel: DataImageModel

    fun bindData(dataImageModel: DataImageModel) {
        this.dataImageModel = dataImageModel
        binding.imageView.setImageBitmap(dataImageModel.bitmap)
        binding.imageViewTitle.text = dataImageModel.timeStamp.toString()
        binding.imageViewSteering.text =
            binding.root.context.getString(R.string.steering, dataImageModel.steering)

        binding.imageViewThrottle.text =
            binding.root.context.getString(R.string.throttle, dataImageModel.throttle)
    }

}
