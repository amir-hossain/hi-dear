package com.hi.dear.source

import java.io.File

interface IRegistrationDataSource {
    suspend fun register(
        userName: String,
        age: String,
        gender: String,
        country: String,
        city: String,
        emailOrMobile: String,
        password: String,
        picture: File
    ): Boolean
}