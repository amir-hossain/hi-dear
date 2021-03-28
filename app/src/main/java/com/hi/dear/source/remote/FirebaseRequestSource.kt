package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.IRequestDataSource
import com.hi.dear.ui.Constant
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.fragment.request.RequestData
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRequestSource : IRequestDataSource {

    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var prefsManager: PrefsManager = PrefsManager.getInstance()

    override suspend fun getRequestData(): MutableList<RequestData>? {
        var userList: MutableList<RequestData> = mutableListOf()
        var mineUserId = prefsManager.readString(PrefsManager.UserId)!!
        firebaseDb.collection(mineUserId + "" + FirebaseConstants.requestReceivedTable_post_fix)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && it.result!!.documents != null) {
                    userList = parseRequestDataFrom(it.result!!.documents)
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return userList
    }

    override suspend fun reactToRequest(accepted: Boolean, requestData: RequestData): RequestData? {
        var status = Constant.requestDeclined
        var result: RequestData? = requestData
        if (accepted) {
            status = Constant.requestAccepted
            result?.status = Constant.requestAccepted
        } else {
            result?.status = Constant.requestDeclined
        }


        var mineUserId = prefsManager.readString(PrefsManager.UserId)!!
        firebaseDb.collection(mineUserId + "" + FirebaseConstants.requestReceivedTable_post_fix)
            .document(requestData.id!!)
            .update(FirebaseConstants.statusField, status)
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
                result = null
            }.await()
        return result
    }

    private fun parseRequestDataFrom(resultList: MutableList<DocumentSnapshot>): MutableList<RequestData> {
        var userList = mutableListOf<RequestData>()
        for (result in resultList) {
            var usr = RequestData()
            usr.id = result[FirebaseConstants.userIdField].toString()
            usr.name = result[FirebaseConstants.userNameField].toString()
            usr.gender = result[FirebaseConstants.genderField].toString()
            usr.picture = result[FirebaseConstants.pictureField].toString()
            usr.status = result[FirebaseConstants.statusField].toString()
            userList.add(usr)
        }

        return userList
    }
}