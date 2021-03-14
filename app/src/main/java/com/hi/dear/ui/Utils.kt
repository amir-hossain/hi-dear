package com.hi.dear.ui

import android.view.View

object Utils {

    fun disableView(view: View) {
        view.isEnabled = false
        view.alpha = .3f
    }

    fun enableView(view: View) {
        view.isEnabled = true
        view.alpha = 1f
    }
}