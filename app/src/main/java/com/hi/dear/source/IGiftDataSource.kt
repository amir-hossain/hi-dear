package com.hi.dear.source

interface IGiftDataSource {
    suspend fun getLastOpenDate(userId: String): String?

    suspend fun setLastOpenDate(userId: String, lastOpenDate: String): Boolean

    suspend fun giftCoin(userId: String, giftCoint: Int): Int
}
