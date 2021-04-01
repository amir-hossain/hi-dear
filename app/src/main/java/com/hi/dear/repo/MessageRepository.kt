package com.hi.dear.repo

import com.hi.dear.source.IMessageDataSource
import com.hi.dear.ui.fragment.message.IMsgListener


class MessageRepository(private val dataSource: IMessageDataSource) : IRepository {

    fun getMessageData(listener: IMsgListener) {
        dataSource.getMessage(listener)
    }
}