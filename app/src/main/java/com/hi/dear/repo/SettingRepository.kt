package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.ISettingDataSource
import com.hi.dear.source.remote.FirebaseSettingSource


class SettingRepository : IRepository {
    private val dataSource: ISettingDataSource = FirebaseSettingSource()
    suspend fun deleteAccount(): RawResult<Boolean> {
        val result = dataSource.deleteAccount()
        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("failed to delete account"))
        }
    }
}