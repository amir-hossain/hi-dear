package com.hi.dear.db.dto

import androidx.room.Dao
import androidx.room.Query
import com.hi.dear.db.entity.UserEntity


@Dao
interface UserDao : BaseDao<UserEntity> {
    @Query("SELECT * FROM user WHERE id = :id and pass = :pass")
    fun getUserBy(id: String, pass:String): UserEntity
}