package com.hi.dear.source

interface ISettingDataSource {
    suspend fun deleteAccount(): Boolean
}
