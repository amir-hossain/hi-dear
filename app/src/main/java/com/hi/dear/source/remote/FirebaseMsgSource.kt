package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.IMessageDataSource
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.fragment.message.MessageData
import kotlinx.coroutines.tasks.await


class FirebaseMsgSource :
    IMessageDataSource {
    private var prefsManager = PrefsManager.getInstance()
    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val mineId = prefsManager.readString(PrefsManager.UserId)

    override suspend fun getMessage(): MutableList<MessageData> {
        var result = mutableListOf<MessageData>()

        firebaseDb.collection(FirebaseConstants.last_message_table_name)
            .whereEqualTo(FirebaseConstants.receiver_id, mineId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    result = getMessageDataFrom(it.result!!.documents)
                }
            }
            .await()
        return result
    }

    private fun getMessageDataFrom(result: MutableList<DocumentSnapshot>): MutableList<MessageData> {
        var dataList = mutableListOf<MessageData>()
        for (document in result) {
            val data = MessageData()
            data.name = document[FirebaseConstants.userNameField].toString()
            data.image = document[FirebaseConstants.pictureField].toString()
            data.message = document[FirebaseConstants.msg].toString()
            dataList.add(data)
        }
        return dataList
    }
}