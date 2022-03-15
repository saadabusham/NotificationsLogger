package com.sedo.notificationlogger.utils.extensions

import androidx.recyclerview.widget.RecyclerView
import com.sedo.notificationlogger.ui.base.BaseBindingRecyclerViewAdapter

fun RecyclerView?.setOnItemClickListener(
    onItemClickListener: BaseBindingRecyclerViewAdapter.OnItemClickListener?
) {
    this?.adapter?.let {
        if (this.adapter is BaseBindingRecyclerViewAdapter<*>) {
            (adapter as BaseBindingRecyclerViewAdapter<*>).itemClickListener = onItemClickListener
        }
    }

}