package com.hi.dear.ui.base

import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    protected fun showToast(msg: Int) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}