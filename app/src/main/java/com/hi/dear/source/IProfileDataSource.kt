package com.hi.dear.source

import com.hi.dear.data.model.common.ProfileData

interface IProfileDataSource {
    suspend fun getProfile(userId: String): ProfileData
    suspend fun saveEditedData(
        newName: String?,
        newAge: String?,
        newCountry: String?,
        newCity: String?,
        newGender: String?,
        newAbout: String?
    ): Boolean
}
