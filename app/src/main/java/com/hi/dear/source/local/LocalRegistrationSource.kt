package com.hi.dear.source.local

import android.app.Application
import com.hi.dear.db.HiDearDB
import com.hi.dear.source.IRegistrationDataSource

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LocalRegistrationSource(context: Application) : IRegistrationDataSource {
    private val dao = HiDearDB.getDatabase(context)?.getUserDao()

    override suspend fun register(
        userName: String,
        age: String,
        gender: String,
        country: String,
        city: String,
        emailOrMobile: String,
        password: String
    ): Boolean {

        /*       val insertId = runBlocking {
                   async(Dispatchers.IO) {
                       return@async dao?.insert(UserEntity(id, name, photo, password))
                   }.await()
               }
               return insertId!! > 0*/
        return true
    }
}