package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.hi.dear.source.IMessageDataSource
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.fragment.message.IMsgListener
import com.hi.dear.ui.fragment.message.MessageData
import timber.log.Timber


class FirebaseMsgSource :
    IMessageDataSource {
    private var prefsManager = PrefsManager.getInstance()
    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val mineId = prefsManager.readString(PrefsManager.UserId)

    override fun getMessage(listener: IMsgListener) {
        var result = mutableListOf<MessageData>()
        firebaseDb.collection(FirebaseConstants.last_message_table_name)
            .whereEqualTo(FirebaseConstants.receiver_id, mineId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Timber.e("listen:error")
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            result.add(getMessageDataFrom(dc.document))
                            listener.incomingMsg(result)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val data = getMessageDataFrom(dc.document)
                            listener.incomingMsg(updateList(result, data))
                        }
                    }
                }

            }
    }

    private fun updateList(
        result: MutableList<MessageData>,
        data: MessageData
    ): MutableList<MessageData> {
        for (i in result.indices) {
            if (result[i].id == data.id) {
                result.removeAt(i)
                result.add(0, data)
            }
        }
        return result
    }

    private fun getMessageDataFrom(snap: QueryDocumentSnapshot): MessageData {
        val data = MessageData()
        data.id = snap[FirebaseConstants.sender_id].toString()
        data.name = snap[FirebaseConstants.userNameField].toString()
        data.picture = snap[FirebaseConstants.pictureField].toString()
        data.message = snap[FirebaseConstants.msg].toString()
        return data
    }
}