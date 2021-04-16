package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.source.ILoginDataSource
import com.hi.dear.ui.FirebaseConstants
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseLoginSource : ILoginDataSource {

    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun login(emailOrMobile: String, password: String): UserCore? {
        return getUserDataIfPassMatch(emailOrMobile, password)
    }

    private suspend fun getUserDataIfPassMatch(emailOrMobile: String, password: String): UserCore? {
        var userData: UserCore? = null
        firebaseDb.collection(FirebaseConstants.authInfoTable).document("$emailOrMobile").get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    if (isPassMatched(it.result!!, password)) {
                        Timber.i("Successful")
                        userData = parseUserFrom(it.result!!)
                    }
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return userData
    }

    private fun isPassMatched(result: DocumentSnapshot, password: String): Boolean {
        val remotePass = result[FirebaseConstants.passwordField] ?: return false
        return remotePass == password
    }

    private fun parseUserFrom(result: DocumentSnapshot): UserCore {
        return UserCore(
            getFieldDataFrom(result, FirebaseConstants.userIdField),
            getFieldDataFrom(result, FirebaseConstants.userNameField),
            getFieldDataFrom(result, FirebaseConstants.pictureField),
            getFieldDataFrom(result, FirebaseConstants.genderField),
            getFieldDataFrom(result, FirebaseConstants.emailOrMobileField)
        )
    }

    private fun getFieldDataFrom(result: DocumentSnapshot, fieldName: String): String {
        val value = result[fieldName]
        return if (value == null) {
            ""
        } else {
            value as String
        }
    }

    override suspend fun logout() {

    }
}