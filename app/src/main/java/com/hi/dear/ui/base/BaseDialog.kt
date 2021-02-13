package com.hi.dear.ui.base

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.hi.dear.ui.TwoButtonDialog

abstract class BaseDialog : DialogFragment() {
    abstract fun showDialog(supportFragmentManager: FragmentManager?)

    companion object {
        const val ARG_MESSAGE = "ARG_MESSAGE"
        const val DIALOG_WINDOW_WIDTH = 0.85
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)

        return onCreateView(inflater,container,savedInstanceState)
    }

    private fun setDialogWindowWidth(width: Double) {
        val window: Window? = dialog?.window
        val size = Point()
        val display: Display
        if (window != null) {
            display = window.windowManager.defaultDisplay
            display.getSize(size)
            val maxWidth: Int = size.x
            window.setLayout((maxWidth * width).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.CENTER)
        }
    }

    override fun onStart() {
        super.onStart()
        setDialogWindowWidth(DIALOG_WINDOW_WIDTH)
    }

    protected fun closeDialog() {
        if (dialog !=null && dialog!!.isShowing) {
            dialog?.dismiss()
        }
    }

    protected fun showDialog(supportFragmentManager: FragmentManager?,tag:String) {
        if (supportFragmentManager != null) {
            this.show(
                supportFragmentManager,
                tag
            )
        } else {
            Log.i(TwoButtonDialog.TAG, "supportFragmentManage is null")
        }
    }
}