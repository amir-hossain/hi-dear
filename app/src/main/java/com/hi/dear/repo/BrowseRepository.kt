package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.source.IBrowseDataSource
import com.hi.dear.source.remote.FirebaseBrowseSource


class BrowseRepository : IRepository {
    private val dataSource: IBrowseDataSource = FirebaseBrowseSource()
    suspend fun getBrowseData(gender: String, limit: Long): RawResult<MutableList<UserCore>> {

        val result = dataSource.getBrowseData(gender, limit)

        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("no user found"))
        }
    }
}