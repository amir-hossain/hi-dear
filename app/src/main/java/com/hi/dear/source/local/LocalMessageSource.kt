package com.hi.dear.source.local

import android.app.Application
import com.hi.dear.db.HiDearDB
import com.hi.dear.source.IMessageDataSource
import com.hi.dear.ui.fragment.message.MessageData

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LocalMessageSource(val context: Application) : IMessageDataSource {
    private val dao = HiDearDB.getDatabase(context)?.getUserDao()


    override suspend fun getMessage(): MutableList<MessageData> {
        return mutableListOf()
    }
}