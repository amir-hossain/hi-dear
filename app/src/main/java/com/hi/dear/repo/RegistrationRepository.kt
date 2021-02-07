package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.source.IRegistrationDataSource


class RegistrationRepository(val dataSource: IRegistrationDataSource) : IRepository {

    // in-memory cache of the loggedInUser object
    private var user: UserCore? = null

    init {
        user = null
    }


    fun register(
        id: String,
        name: String,
        photo: String,
        password: String,
        conPassword: String
    ): RawResult<Boolean> {
        val result = dataSource.register(id, name, photo, password, conPassword)
        return if (result) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("user registration is not successful"))
        }
    }

    private fun setLoggedInUser(UserCore: UserCore) {
        this.user = UserCore
    }
}