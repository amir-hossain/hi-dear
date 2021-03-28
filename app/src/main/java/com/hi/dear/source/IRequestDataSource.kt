package com.hi.dear.source


import com.hi.dear.ui.fragment.request.RequestData

interface IRequestDataSource {
    suspend fun getRequestData(): MutableList<RequestData>?
}
