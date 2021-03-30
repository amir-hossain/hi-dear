package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IChatDataSource
import com.hi.dear.source.remote.FirebaseChatSource
import com.hi.dear.ui.activity.chat.IChatListener


class ChatRepository : IRepository {
    private val dataSource: IChatDataSource by lazy { FirebaseChatSource() }

    suspend fun sendMessage(text: String, otherUserId: String): RawResult<Boolean> {
        val result = dataSource.sendMessage(text, otherUserId)
        return if (result) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("send message is not successful"))
        }
    }

    fun getMessage(otherUserId: String, listener: IChatListener) {
        dataSource.getMessage(otherUserId, listener)
    }
}