package com.hi.dear.source


import com.hi.dear.ui.fragment.match.RequestData

interface IRequestDataSource {
    suspend fun getRequestData(): MutableList<RequestData>?
    suspend fun reactToRequest(accepted: Boolean, requestData: RequestData): RequestData?
}
