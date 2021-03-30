package com.hi.dear.source

import com.hi.dear.ui.activity.chat.IChatListener

interface IChatDataSource {
    suspend fun sendMessage(text: String, otherUserId: String): Boolean
    fun getMessage(otherUserId: String, listener: IChatListener)
}