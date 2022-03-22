package com.hi.dear.ui

import androidx.annotation.StringRes
import com.hi.dear.ui.base.BaseDialog


object DialogFactory {
    fun makeDialog(
        messageId: String,
        listener: IBaseListener
    ): BaseDialog {
        if (listener is ITwoBtnListener) {
            return TwoButtonDialog.newInstance(messageRes = messageId, listener = listener)
        } else if (listener is ISingleBtnListener) {
            return SingleButtonDialog.newInstance(messageRes = messageId, listener = listener)
        }
        throw RuntimeException("Please use other than base listener")
    }

    fun makeDialog(
        messageId: String,
        listener: IBaseListener,
        icon:Int
    ): BaseDialog {
        if (listener is ITwoBtnListener) {
            return TwoButtonDialog.newInstance(messageRes = messageId, listener = listener)
        } else if (listener is ISingleBtnListener) {
            return SingleButtonDialog.newInstance(messageRes = messageId, listener = listener, icon = icon)
        }
        throw RuntimeException("Please use other than base listener")
    }

    interface ITwoBtnListener : IBaseListener {
        fun onPositiveBtnClicked()
        fun onNegativeBtnClicked()
    }

    interface ISingleBtnListener : IBaseListener {
        fun onPositiveBtnClicked()
    }

    interface IBaseListener
}