package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IMessageDataSource
import com.hi.dear.ui.fragment.message.MessageData


class MessageRepository(private val dataSource: IMessageDataSource) : IRepository {

    fun getMessageData(): RawResult<List<MessageData>> {

        val result = dataSource.getData()

        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("no data found"))
        }
    }
}