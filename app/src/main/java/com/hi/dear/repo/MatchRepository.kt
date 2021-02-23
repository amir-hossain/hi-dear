package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IMatchDataSource
import com.hi.dear.ui.fragment.request.RequestData


class MatchRepository(private val dataSource: IMatchDataSource) : IRepository {

    fun getMatchData(): RawResult<MutableList<RequestData>> {

        val result = dataSource.getData()

        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("no data found"))
        }
    }
}