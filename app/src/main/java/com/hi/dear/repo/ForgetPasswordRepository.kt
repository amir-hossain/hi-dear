package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IForgetPassDataSource


class ForgetPasswordRepository(private val dataSource: IForgetPassDataSource) : IRepository {

    fun forgetPassword(email: String): RawResult<Boolean> {

        val result = dataSource.forgetPassword(email)

        return if (result) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("unable to send"))
        }
    }
}