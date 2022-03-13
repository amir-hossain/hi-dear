package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IGiftDataSource


class GiftRepository(private val dataSource: IGiftDataSource) : IRepository {

    suspend fun getLastOpenDate(userId: String): RawResult<String> {

        val result = dataSource.getLastOpenDate(userId)

        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("no data found"))
        }
    }

    suspend fun setLastOpenDate(userId: String, lastOpenDate: String): RawResult<Boolean> {

        val result = dataSource.setLastOpenDate(userId,lastOpenDate)

        return RawResult.Success(result)
    }
}