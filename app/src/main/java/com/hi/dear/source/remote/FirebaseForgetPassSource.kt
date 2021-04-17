package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.IForgetPassDataSource
import com.hi.dear.ui.FirebaseConstants
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseForgetPassSource : IForgetPassDataSource {
    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun getFieldDataFrom(result: DocumentSnapshot, fieldName: String): String {
        val value = result[fieldName]
        return if (value == null) {
            ""
        } else {
            value as String
        }
    }

    override suspend fun forgetPassword(emailOrMobile: String): String? {
        var password: String? = null
        firebaseDb.collection(FirebaseConstants.authInfoTable).document("$emailOrMobile").get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    Timber.i("Successful")
                    password = getFieldDataFrom(it.result!!, FirebaseConstants.passwordField)
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return password
    }
}