package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.source.IProfileDataSource
import com.hi.dear.source.remote.FirebaseProfileSource


class ProfileRepository : IRepository {
    private val dataSource: IProfileDataSource = FirebaseProfileSource()
    suspend fun getProfileData(userId: String): RawResult<ProfileData> {
        val result = dataSource.getProfile(userId)
        return if (result?.id != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("user data fetch failed"))
        }
    }

    suspend fun saveEditedData(
        newName: String?, newAge: String?, newCountry: String?, newCity: String?,
        newGender: String?, newAbout: String?
    ): RawResult<Boolean> {
        val result = dataSource.saveEditedData(
            newName, newAge, newCountry, newCity, newGender,
            newAbout
        )
        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("user data fetch failed"))
        }
    }
}