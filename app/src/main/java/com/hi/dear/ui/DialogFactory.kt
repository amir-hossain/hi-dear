package com.hi.dear.ui

import androidx.annotation.StringRes
import com.hi.dear.ui.base.BaseDialog


object DialogFactory {
    fun makeDialog(
        @StringRes messageId: Int,
        listener : IBaseListener
    ): BaseDialog {
        if(listener is ITwoBtnListener){
            return TwoButtonDialog.newInstance(messageRes = messageId, listener = listener)
        }
           throw RuntimeException("Please use other than base listener")
    }

    interface ITwoBtnListener : IBaseListener {
        fun onPositiveBtnClicked()
        fun onNegativeBtnClicked()
    }

    interface IBaseListener {}
}