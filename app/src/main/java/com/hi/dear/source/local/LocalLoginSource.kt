package com.hi.dear.source.local

import android.app.Application
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.db.HiDearDB
import com.hi.dear.source.ILoginDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LocalLoginSource(val context: Application) : ILoginDataSource {
    private val dao = HiDearDB.getDatabase(context)?.getUserDao()
    override suspend fun login(id: String, password: String): UserCore? {
        val userEntity = runBlocking {
            async(Dispatchers.IO) {
                return@async dao?.getUserBy(id, password)
            }.await()
        }
        return if (userEntity == null) {
            null
        } else {
            UserCore(userEntity.id, userEntity.name)
        }
    }

    override suspend fun logout() {

    }
}