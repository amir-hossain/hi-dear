package com.hi.dear.source

import com.hi.dear.data.model.common.UserCore

interface ILoginDataSource {
    fun login(id: String, password: String): UserCore?

    fun logout()
}
