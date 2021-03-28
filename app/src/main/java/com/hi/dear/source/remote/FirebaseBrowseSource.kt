package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.source.IBrowseDataSource
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.PrefsManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseBrowseSource : IBrowseDataSource {


    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var prefsManager: PrefsManager = PrefsManager.getInstance()

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

    override suspend fun sendRequest(receiverUserData: UserCore): Boolean {
        val mineId = prefsManager.readString(PrefsManager.UserId)
        var savedSentInfo = false
        var savedReceivedInfo = false
        firebaseDb.collection(mineId!! + "" + FirebaseConstants.sentRequestTable_post_fix)
            .document(receiverUserData.id!!)
            .set(getHashMapFrom(receiverUserData))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    savedSentInfo = true
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()

        firebaseDb.collection(receiverUserData.id!! + "" + FirebaseConstants.requestReceivedTable_post_fix)
            .document(mineId)
            .set(getMineHasMap())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    savedReceivedInfo = true
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return savedReceivedInfo && savedSentInfo
    }

    private fun getMineHasMap(): HashMap<String, Any> {
        val userInfo = HashMap<String, Any>()
        userInfo[FirebaseConstants.userIdField] = prefsManager.readString(PrefsManager.UserId)!!
        userInfo[FirebaseConstants.userNameField] = prefsManager.readString(PrefsManager.UserName)!!
        userInfo[FirebaseConstants.pictureField] = prefsManager.readString(PrefsManager.Pic)!!
        userInfo[FirebaseConstants.genderField] = prefsManager.readString(PrefsManager.Gender)!!
        return userInfo
    }

    private fun getHashMapFrom(userData: UserCore): HashMap<String, Any> {
        val userInfo = HashMap<String, Any>()
        userInfo[FirebaseConstants.userIdField] = userData.id!!
        userInfo[FirebaseConstants.userNameField] = userData.name!!
        userInfo[FirebaseConstants.genderField] = userData.gender!!
        userInfo[FirebaseConstants.pictureField] = userData.picture!!
        return userInfo
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