package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IRequestDataSource
import com.hi.dear.ui.fragment.request.RequestData


class RequestRepository(private val dataSource: IRequestDataSource) : IRepository {

    suspend fun getRequestData(): RawResult<MutableList<RequestData>> {

        val result = dataSource.getRequestData()

        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("no data found"))
        }
    }
}