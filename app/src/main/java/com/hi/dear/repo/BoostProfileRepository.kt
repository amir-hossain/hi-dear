package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IBoostProfileDataSource
import com.hi.dear.ui.fragment.top.TopProfileData

class BoostProfileRepository(private val dataSource: IBoostProfileDataSource) : IRepository {

    suspend fun boostProfile(data: TopProfileData): RawResult<Long> {
        val result = dataSource.boostProfile(data)

        return RawResult.Success(result)
    }
}