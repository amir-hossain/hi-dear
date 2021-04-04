package com.hi.dear.source.local

import android.app.Application
import com.hi.dear.db.HiDearDB
import com.hi.dear.source.IMessageDataSource
import com.hi.dear.ui.activity.message.IMsgListener

class LocalMessageSource(val context: Application) : IMessageDataSource {
    private val dao = HiDearDB.getDatabase(context)?.getUserDao()

    override fun getMessage(listener: IMsgListener) {}
}