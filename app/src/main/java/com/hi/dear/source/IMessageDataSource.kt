package com.hi.dear.source


import com.hi.dear.ui.fragment.message.IMsgListener

interface IMessageDataSource {
    fun getMessage(listener: IMsgListener)
}
