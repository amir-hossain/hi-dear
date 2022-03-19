package com.hi.dear.source

import com.hi.dear.ui.fragment.top.TopProfileData

interface IBoostProfileDataSource {
    suspend fun boostProfile(data: TopProfileData): Long

    suspend fun deductCoin(coin: Int,userId: String): Int
}
