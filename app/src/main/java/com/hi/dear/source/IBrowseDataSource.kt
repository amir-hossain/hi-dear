package com.hi.dear.source

import com.hi.dear.data.model.common.UserCore

interface IBrowseDataSource {
    suspend fun getBrowseData(preferredGender: String, limit: Long): MutableList<UserCore>

    suspend fun sendRequest(receiverUserData: UserCore): Boolean
}
