package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.source.ILoginDataSource



class LoginRepository(val dataSource: ILoginDataSource) : IRepository {

    // in-memory cache of the loggedInUser object
    private var user: UserCore? = null

    init {
        user = null
    }

    fun login(id: String, password: String): RawResult<UserCore> {

        val result = dataSource.login(id, password)

        return if (result  != null) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("no user found"))
        }
    }
}