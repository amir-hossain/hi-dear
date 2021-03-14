package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.source.ILoginDataSource
import com.hi.dear.source.remote.FirebaseLoginSource


class LoginRepository : IRepository {
    private val dataSource: ILoginDataSource = FirebaseLoginSource()
    suspend fun login(id: String, password: String): RawResult<UserCore> {

        val result = dataSource.login(id, password)

        return if (result != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("no user found"))
        }
    }
}