package com.sedo.notificationlogger.utils

import android.view.View
import android.view.ViewGroup

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.disableView() {
    isEnabled = false
}

fun View.enableView() {
    isEnabled = true
}


fun View.disableViews() {
    val layout = this as ViewGroup
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        child.isEnabled = false
        if (child is ViewGroup) {
            child.disableViews()
        }
    }

}

fun View.enableViews() {
    val layout = this as ViewGroup
    for (i in 0 until layout.childCount) {
        val child = layout.getChildAt(i)
        child.isEnabled = true
        if (child is ViewGroup) {
            child.enableViews()
        }
    }
}