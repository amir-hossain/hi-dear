package com.hi.dear.repo

import com.hi.dear.data.RawResult
import com.hi.dear.source.IForgetPassDataSource

class ForgetPasswordRepository(private val dataSource: IForgetPassDataSource) : IRepository {
    suspend fun forgetPassword(email: String): RawResult<String> {
        val result = dataSource.forgetPassword(email)
        return if (result != null && result.isNotBlank()) {
            RawResult.Success(result)
        } else {
            RawResult.Error(RuntimeException("user not found"))
        }
    }
}