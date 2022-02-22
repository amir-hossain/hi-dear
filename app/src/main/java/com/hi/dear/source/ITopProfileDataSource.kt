package com.hi.dear.source

import com.hi.dear.ui.fragment.top.TopProfileData

interface ITopProfileDataSource {
    suspend fun getTopProfile(): MutableList<TopProfileData>?
}
