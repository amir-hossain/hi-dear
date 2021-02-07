package com.hi.dear.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hi.dear.db.dto.UserDao
import com.hi.dear.db.entity.UserEntity


@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = true
)

abstract class HiDearDB : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object{
        private val DB_NAME = "hi_dear_db"
        private var instance: HiDearDB? = null

        fun getDatabase(context: Context): HiDearDB? {
            if(instance == null){
                synchronized(HiDearDB::class.java){
                    if(instance == null){
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            HiDearDB::class.java,
                            DB_NAME
                        ).build()
                    }
                }
            }
            return instance
        }
    }
}