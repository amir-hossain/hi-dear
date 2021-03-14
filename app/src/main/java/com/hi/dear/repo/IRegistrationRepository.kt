package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IRegistrationDataSource
import com.hi.dear.source.remote.FirebaseRegistrationSource


class IRegistrationRepository : IRepository {
    private val dataSource: IRegistrationDataSource by lazy { FirebaseRegistrationSource() }
    suspend fun register(
        userName: String,
        age: String,
        gender: String,
        country: String,
        city: String,
        emailOrMobile: String,
        password: String
    ): RawResult<Boolean> {
        val result =
            dataSource.register(
                userName = userName, age = age, gender = gender, country = country,
                city = city, emailOrMobile = emailOrMobile, password = password
            )
        result
        return if (result) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("user registration is not successful"))
        }
    }
}