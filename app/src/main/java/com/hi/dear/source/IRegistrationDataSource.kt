package com.hi.dear.source

interface IRegistrationDataSource {
    suspend fun register(
        userName: String,
        age: String,
        gender: String,
        country: String,
        city: String,
        emailOrMobile: String,
        password: String
    ): Boolean
}