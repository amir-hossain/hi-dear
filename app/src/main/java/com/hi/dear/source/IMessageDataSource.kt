package com.hi.dear.source


import com.hi.dear.ui.activity.message.IMsgListener

interface IMessageDataSource {
    fun getMessage(listener: IMsgListener)
}
