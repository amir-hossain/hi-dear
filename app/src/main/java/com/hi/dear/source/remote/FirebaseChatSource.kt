package com.hi.dear.source.remote


import com.google.firebase.firestore.*
import com.hi.dear.data.model.common.Chat
import com.hi.dear.source.IChatDataSource
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.chat.IChatListener
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class FirebaseChatSource :
    IChatDataSource {
    private var prefsManager = PrefsManager.getInstance()
    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val mineId = prefsManager.readString(PrefsManager.UserId)!!
    override suspend fun sendMessage(text: String, otherUserId: String): Boolean {
        var messageId = Utils.getHash(System.currentTimeMillis().toString())
        val isChatSaved = saveMsgToChatTable(otherUserId, text, messageId)
        val isLastMsgSaved = saveLastMsg(otherUserId, text)
        return isChatSaved && isLastMsgSaved
    }

    private suspend fun saveLastMsg(otherUserId: String, text: String): Boolean {
        var result = false
        firebaseDb.collection(FirebaseConstants.last_message_table_name)
            .document(mineId)
            .set(getMsgMap(text, otherUserId))
            .addOnCompleteListener {
                Timber.i("last message saved successfully")
                result = true
            }
            .addOnFailureListener {
                Timber.i("last message save failed")
            }.await()
        return result
    }

    private fun getMsgMap(text: String, otherUserId: String): HashMap<String, Any> {
        val result = HashMap<String, Any>()
        result[FirebaseConstants.pictureField] = prefsManager.readString(PrefsManager.Pic)!!
        result[FirebaseConstants.userNameField] = prefsManager.readString(PrefsManager.UserName)!!
        result[FirebaseConstants.sender_id] = mineId
        result[FirebaseConstants.receiver_id] = otherUserId
        result[FirebaseConstants.msg] = text
        return result
    }

    private suspend fun saveMsgToChatTable(
        otherUserId: String, text: String, msgId: String
    ): Boolean {
        var result = false
        val tableId = getSingleHashCodeFrom(otherUserId, mineId)
        val tableName = "${tableId}${FirebaseConstants.chat_table_post_fix}"
        firebaseDb.collection(tableName)
            .document(msgId)
            .set(getChatMap(text, mineId, msgId))
            .addOnCompleteListener {
                Timber.i("message send successfully")
                result = true
            }
            .addOnFailureListener {
                Timber.i("message send failed")
            }.await()
        return result
    }

    private fun getChatMap(msg: String, senderId: String, chatId: String): HashMap<String, Any> {
        val result = HashMap<String, Any>()
        result[FirebaseConstants.chat_id] = chatId
        result[FirebaseConstants.sender_id] = senderId
        result[FirebaseConstants.userNameField] = prefsManager.readString(PrefsManager.UserName)!!
        result[FirebaseConstants.pictureField] = prefsManager.readString(PrefsManager.Pic)!!
        result[FirebaseConstants.msg] = msg
        result[FirebaseConstants.time_field] = System.currentTimeMillis()
        return result
    }

    override fun getMessage(otherUserId: String, listener: IChatListener) {
        var result = mutableListOf<Chat>()
        val tableId = getSingleHashCodeFrom(otherUserId, mineId)
        val tableName = "${tableId}${FirebaseConstants.chat_table_post_fix}"
        firebaseDb.collection(tableName)
            .orderBy(FirebaseConstants.time_field)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Timber.e("listen:error")
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> result.add(getChatDataFrom(dc.document))
                    }
                }
                listener.incomingChat(result)
            }
    }


    private fun getChatDataFrom(document: QueryDocumentSnapshot): Chat {
        val result = Chat()
        result.text = document[FirebaseConstants.msg].toString()
        result.senderId = document[FirebaseConstants.sender_id].toString()
        result.name = document[FirebaseConstants.userNameField].toString()
        result.photoUrl = document[FirebaseConstants.pictureField].toString()
        return result
    }

    private fun getSingleHashCodeFrom(otherUserId: String, mineId: String): String {
        val ids = arrayOf(otherUserId, mineId)
        ids.sort()
        val hash1 = Utils.getHash(ids[0])
        val hash2 = Utils.getHash(ids[1])
        return Utils.getHash("$hash1$hash2")
    }
}