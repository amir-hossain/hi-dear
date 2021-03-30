package com.hi.dear.ui.activity.chat

import com.hi.dear.data.model.common.Chat

interface IChatListener {
    fun incomingChat(messageList: MutableList<Chat>)
    fun chatLoadFailed(messageList: MutableList<Chat>)
}