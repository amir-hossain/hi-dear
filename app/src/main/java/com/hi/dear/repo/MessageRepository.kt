package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IMessageDataSource
import com.hi.dear.ui.fragment.message.MessageData


class MessageRepository(private val dataSource: IMessageDataSource) : IRepository {

    suspend fun getMessageData(): RawResult<MutableList<MessageData>> {

        val result = dataSource.getMessage()

        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("no data found"))
        }
    }
}