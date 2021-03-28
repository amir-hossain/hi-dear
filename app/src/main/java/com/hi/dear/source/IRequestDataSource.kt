package com.hi.dear.source


import com.hi.dear.ui.fragment.request.RequestData

interface IRequestDataSource {
    suspend fun getRequestData(): MutableList<RequestData>?
    suspend fun reactToRequest(accepted: Boolean, requestData: RequestData): RequestData?
}
