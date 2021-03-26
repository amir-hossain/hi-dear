package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.source.IBrowseDataSource
import com.hi.dear.ui.FirebaseConstants
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseBrowseSource : IBrowseDataSource {

    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun getBrowseData(
        preferredGender: String,
        limit: Long
    ): MutableList<UserCore> {
        var userList: MutableList<UserCore> = mutableListOf()
        firebaseDb.collection(FirebaseConstants.userInfoTable)
            .whereEqualTo(FirebaseConstants.genderField, preferredGender)
            .orderBy(FirebaseConstants.userNameField).limit(limit)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && it.result!!.documents != null) {
                    userList = parseUserFrom(it.result!!.documents)
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return userList
    }

    override suspend fun sendMatch(): Boolean {
        return false
    }

    private fun parseUserFrom(resultList: MutableList<DocumentSnapshot>): MutableList<UserCore> {
        var userList = mutableListOf<UserCore>()
        for (result in resultList) {
            var usr = UserCore()
            usr.id = result[FirebaseConstants.userIdField].toString()
            usr.name = result[FirebaseConstants.userNameField].toString()
            usr.gender = result[FirebaseConstants.genderField].toString()
            usr.picture = result[FirebaseConstants.pictureField].toString()
            userList.add(usr)
        }

        return userList
    }
}