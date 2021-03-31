package com.hi.dear.source


import com.hi.dear.ui.fragment.message.MessageData

interface IMessageDataSource {
    suspend fun getMessage(): MutableList<MessageData>
}
