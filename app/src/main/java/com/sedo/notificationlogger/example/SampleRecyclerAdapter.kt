package com.sedo.notificationlogger.example

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sedo.notificationlogger.example.databinding.RowSampleItemBinding
import com.sedo.notificationlogger.ui.base.BaseBindingRecyclerViewAdapter
import com.sedo.notificationlogger.ui.base.BaseViewHolder


class SampleRecyclerAdapter constructor(
    context: Context
) : BaseBindingRecyclerViewAdapter<Int>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowSampleItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowSampleItemBinding) :
        BaseViewHolder<Int>(binding.root) {

        override fun bind(item: Int) {
            binding?.imgSample.setImageResource(item)
            binding.root.setOnLongClickListener {
                itemClickListener?.onItemLongClick(it, adapterPosition, item)
                return@setOnLongClickListener true
            }
        }
    }
}