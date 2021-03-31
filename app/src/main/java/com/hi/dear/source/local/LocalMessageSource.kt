package com.hi.dear.source.local

import android.app.Application
import com.hi.dear.db.HiDearDB
import com.hi.dear.source.IMessageDataSource
import com.hi.dear.ui.fragment.message.MessageData

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LocalMessageSource(val context: Application) : IMessageDataSource {
    private val dao = HiDearDB.getDatabase(context)?.getUserDao()


    override suspend fun getMessage(): MutableList<MessageData> {
        var dataList = ArrayList<MessageData>()
        dataList.add(
            MessageData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                true,
                false,
                "Benjamin",
                "Hey! how was the concert last night?? ",
                null,
                "09.40am"
            )
        )
        dataList.add(
            MessageData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                true, false, "Benjamin", "Hey! how was the concert last night?? ",
                null, "09.40am"
            )
        )
        dataList.add(
            MessageData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                false, false, "Benjamin", "Where are you from? ",
                7, "09.12am"
            )
        )
        dataList.add(
            MessageData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                true, false, "GeorgeClooney", "Hello! :)",
                null, "Yesterday"
            )
        )
        dataList.add(
            MessageData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                true, true, "Chris_London", "Yeah I’ll let you know when I’m around",
                null, "Yesterday"
            )
        )
        dataList.add(
            MessageData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                true, false, "LatinLover", "Hey! how you doing today? Lets go out ",
                null, "16 Sep 2016"
            )
        )
        dataList.add(
            MessageData(
                "https://i.picsum.photos/id/397/200/300.jpg?hmac=9VBInLrifj_yyc2JuJSAVIfj9yQdt5Ovm2sHmvva-48",
                false, false, "SweetGuy_nyc", "Nice smile!",
                null, "14 Sep 2016"
            )
        )
        return dataList
    }
}