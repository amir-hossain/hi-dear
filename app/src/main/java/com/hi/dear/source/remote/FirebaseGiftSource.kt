package com.hi.dear.source.remote


import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.IGiftDataSource
import com.hi.dear.ui.FirebaseConstants
import kotlinx.coroutines.tasks.await

class FirebaseGiftSource : IGiftDataSource {
    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun getLastOpenDate(userId: String): String {
        var result = ""
        firebaseDb.collection(FirebaseConstants.gifTable).document(userId)
            .get()
            .addOnSuccessListener {
                if (it.data != null) {
                    result = it.data!![FirebaseConstants.giftField].toString()
                }
            }.await()
        return result
    }

    override suspend fun setLastOpenDate(userId: String, lastOpenDate: String): Boolean {
        var result = false
        firebaseDb.collection(FirebaseConstants.gifTable).document(userId)
            .set(hashMapOf(FirebaseConstants.giftField to lastOpenDate))
            .addOnSuccessListener {
                result = true
            }.await()
        return result
    }
}