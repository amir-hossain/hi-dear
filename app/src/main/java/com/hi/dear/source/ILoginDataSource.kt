package com.hi.dear.source

import com.hi.dear.data.model.common.UserCore

interface ILoginDataSource {
    suspend fun login(emailOrMobile: String, password: String): UserCore?

    suspend fun logout()
}
