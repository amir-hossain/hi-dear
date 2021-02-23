package com.hi.dear.source.local

import android.app.Application
import com.hi.dear.db.HiDearDB
import com.hi.dear.source.IMatchDataSource
import com.hi.dear.ui.fragment.request.RequestData

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LocalMatchSource(val context: Application) : IMatchDataSource {
    private val dao = HiDearDB.getDatabase(context)?.getUserDao()


    override fun getData(): MutableList<RequestData>? {
        var dataList = ArrayList<RequestData>()
        dataList.add(
            RequestData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                "Benjamin",
                null
            )
        )
        dataList.add(
            RequestData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                "Mark",
                "Added on 12 Sep"
            )
        )
        dataList.add(
            RequestData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                "Tim_89",
                "Added on 3 Sep",
            )
        )

        dataList.add(
            RequestData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                "ThePeter",
                "Added on 22 Aug",
            )
        )
        dataList.add(
            RequestData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                "Jimbo",
                "Added on 21 Aug",
            )
        )
        dataList.add(
            RequestData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                "CoolDude",
                "Added on 12 Aug",
            )
        )
        return dataList
    }
}