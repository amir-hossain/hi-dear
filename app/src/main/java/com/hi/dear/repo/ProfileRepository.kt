package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.source.IProfileDataSource
import com.hi.dear.source.remote.FirebaseProfileSource


class ProfileRepository : IRepository {
    private val dataSource: IProfileDataSource = FirebaseProfileSource()
    suspend fun getProfileData(userId: String): RawResult<ProfileData> {
        val result = dataSource.getProfile(userId)
        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("user data fetch failed"))
        }
    }
}