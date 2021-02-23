package com.hi.dear.source


import com.hi.dear.ui.fragment.request.RequestData

interface IMatchDataSource {
    fun getData(): MutableList<RequestData>?
}
