package com.hi.dear.source

interface IForgetPassDataSource {
    suspend fun forgetPassword(email: String): String?
}
