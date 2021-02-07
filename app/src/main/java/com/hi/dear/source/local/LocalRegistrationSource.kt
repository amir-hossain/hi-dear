package com.hi.dear.source.local

import android.app.Application
import com.hi.dear.db.HiDearDB
import com.hi.dear.db.entity.UserEntity
import com.hi.dear.source.IRegistrationDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LocalRegistrationSource(context: Application) : IRegistrationDataSource {
    private val dao = HiDearDB.getDatabase(context)?.getUserDao()

    override fun register(
        id: String, name: String, photo: String, password: String,
        conPass: String
    ): Boolean {

        val insertId = runBlocking {
            async(Dispatchers.IO) {
                return@async dao?.insert(UserEntity(id, name, photo, password))
            }.await()
        }
        return insertId!! > 0
    }
}