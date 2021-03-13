package com.hi.dear.ui

import android.view.View

object Utils {
    fun formatLogMessage(methodName: String, keyWord: String, value: Any): String {
        return "$methodName() called with $keyWord = [ $value ]"
    }

    fun disableView(view: View) {
        view.isEnabled = false
        view.alpha = .3f
    }

    fun enableView(view: View) {
        view.isEnabled = true
        view.alpha = 1f
    }
}