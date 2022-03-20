package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.ITopProfileDataSource
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.fragment.top.TopProfileData
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseTopProfileSource : ITopProfileDataSource {

    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun getTopProfile(): MutableList<TopProfileData> {
        var userList: MutableList<TopProfileData> = mutableListOf()
        firebaseDb.collection(FirebaseConstants.boostedTable)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    userList = parseRequestDataFrom(it.result!!.documents)
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return userList
    }

    private fun parseRequestDataFrom(resultList: MutableList<DocumentSnapshot>): MutableList<TopProfileData> {
        var userList = mutableListOf<TopProfileData>()
        for (result in resultList) {
            var usr = TopProfileData()
            usr.id = result[FirebaseConstants.userIdField].toString()
            usr.name = result[FirebaseConstants.userNameField].toString()
            usr.gender = result[FirebaseConstants.genderField].toString()
            usr.picture = result[FirebaseConstants.pictureField].toString()
            usr.endTime = result[FirebaseConstants.endTimeField].toString().toLong()
            userList.add(usr)
        }

        return userList
    }
}