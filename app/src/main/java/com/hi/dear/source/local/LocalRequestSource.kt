package com.hi.dear.source.local

import android.app.Application
import com.hi.dear.db.HiDearDB
import com.hi.dear.source.IRequestDataSource
import com.hi.dear.ui.fragment.request.RequestData

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LocalRequestSource(val context: Application) : IRequestDataSource {
    private val dao = HiDearDB.getDatabase(context)?.getUserDao()
    override suspend fun getRequestData(): MutableList<RequestData>? {
        return null
    }

    override suspend fun reactToRequest(accepted: Boolean, requestData: RequestData): RequestData? {
        return null
    }

}