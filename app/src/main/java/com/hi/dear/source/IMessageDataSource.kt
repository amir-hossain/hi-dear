package com.hi.dear.source


import com.hi.dear.ui.fragment.message.MessageData

interface IMessageDataSource {
    fun getData(): List<MessageData>?
}
