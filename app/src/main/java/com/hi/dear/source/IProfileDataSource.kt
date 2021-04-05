package com.hi.dear.source

import com.hi.dear.data.model.common.ProfileData

interface IProfileDataSource {
    suspend fun getProfile(userId: String): ProfileData
}
