package com.hi.dear.source.local

import android.app.Application
import com.hi.dear.db.HiDearDB
import com.hi.dear.source.IForgetPassDataSource

class ForgetPassDataSource(val context: Application) : IForgetPassDataSource {
    private val dao = HiDearDB.getDatabase(context)?.getUserDao()
    override fun forgetPassword(email: String): Boolean {
        return true
    }

}