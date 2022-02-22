package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.ITopProfileDataSource
import com.hi.dear.ui.fragment.top.TopProfileData


class TopProfileRepository(private val dataSource: ITopProfileDataSource) : IRepository {
    suspend fun getTopProfile(): RawResult<MutableList<TopProfileData>> {
        val result = dataSource.getTopProfile()
        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("no data found"))
        }
    }
}