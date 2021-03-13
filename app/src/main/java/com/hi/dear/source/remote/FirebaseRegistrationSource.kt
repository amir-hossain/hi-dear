package com.hi.dear.source.remote


import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.IRegistrationDataSource
import com.hi.dear.ui.Utils
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRegistrationSource :
    IRegistrationDataSource {

    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var userInfoResult = false
    private var authResult = false

    override suspend fun register(
        userName: String,
        age: String,
        gender: String,
        country: String,
        city: String,
        emailOrMobile: String,
        password: String
    ): Boolean {
        val userInfo = HashMap<String, Any>()
        userInfo["userName"] = userName
        userInfo["age"] = age
        userInfo["gender"] = gender
        userInfo["country"] = country
        userInfo["city"] = city

        saveUserInfo(userInfo)
        saveAuthInfo(emailOrMobile, password)

        return userInfoResult && authResult
    }

    private suspend fun saveUserInfo(userInfo: HashMap<String, Any>) {
        firebaseDb.collection("userInfo").document().set(userInfo).addOnCompleteListener {
            if (it.isSuccessful) {
                userInfoResult = true
                Timber.i(Utils.formatLogMessage("register", "isSuccessful", it.isSuccessful))
            }
        }.addOnFailureListener {
            userInfoResult = true
            Timber.i(
                Utils.formatLogMessage(
                    "register",
                    "addOnFailureListener",
                    it.localizedMessage
                )
            )
        }.await()
    }

    private suspend fun saveAuthInfo(emailOrMobile: String, password: String) {
        val authInfo = HashMap<String, Any>()
        authInfo["emailOrMobile"] = emailOrMobile
        authInfo["password"] = password
        firebaseDb.collection("authInfo").document("$emailOrMobile").set(authInfo)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    authResult = true
                    Timber.i(
                        Utils.formatLogMessage(
                            "saveAuthInfo",
                            "isSuccessful",
                            it.isSuccessful
                        )
                    )
                }
            }.addOnFailureListener {
            authResult = true
            Timber.i(
                Utils.formatLogMessage(
                    "saveAuthInfo",
                    "addOnFailureListener",
                    it.localizedMessage
                )
            )
        }.await()
    }
}