package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.source.IBrowseDataSource
import com.hi.dear.source.remote.FirebaseBrowseSource


class BrowseRepository : IRepository {
    private val dataSource: IBrowseDataSource = FirebaseBrowseSource()
    suspend fun getBrowseData(gender: String, limit: Long): RawResult<MutableList<UserCore>> {

        val result = dataSource.getBrowseData(gender, limit)

        return RawResult.Success(result)
    }

    suspend fun sendRequest(receiverUserData: UserCore): RawResult<Boolean> {
        val result = dataSource.sendRequest(receiverUserData)
        return RawResult.Success(result)
    }

    suspend fun getRemainingCoin(userId: String): RawResult<Int> {
        val result = dataSource.getRemainingCoin(userId)

        return RawResult.Success(result)
    }

    suspend fun deductCoin(coinOfRequest: Int,userId: String): RawResult<Int> {
        val result = dataSource.deductCoin(coinOfRequest,userId)

        return RawResult.Success(result)
    }

    suspend fun giftCoin(giftCoint: Int, userId: String): RawResult<Int> {
        val result = dataSource.giftCoin(userId,giftCoint)

        return RawResult.Success(result)
    }
}