package com.hi.dear.source.remote


import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.ISettingDataSource
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.PrefsManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseSettingSource : ISettingDataSource {

    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun deleteAccount(): Boolean {
        var userInfoResult = deleteForm(FirebaseConstants.userInfoTable)
        var authInfoResult = deleteAuthTable()
        var messageResult = deleteForm(FirebaseConstants.last_message_table_name)
        return userInfoResult && authInfoResult && messageResult
    }

    private suspend fun deleteAuthTable(): Boolean {
        var result = false
        val myEmailOrMobile = PrefsManager.getInstance().readString(PrefsManager.emailOrMobile)!!
        firebaseDb.collection(FirebaseConstants.authInfoTable).document("$myEmailOrMobile")
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    result = true
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return result
    }

    private suspend fun deleteForm(tableName: String): Boolean {
        var result = false
        val myId = PrefsManager.getInstance().readString(PrefsManager.UserId)!!
        firebaseDb.collection(tableName).document("$myId")
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    result = true
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return result
    }
}