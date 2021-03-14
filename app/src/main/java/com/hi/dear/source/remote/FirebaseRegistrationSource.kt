package com.hi.dear.source.remote


import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.IRegistrationDataSource
import com.hi.dear.ui.FirebaseConstants
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap

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
        val userId = getUserId()
        userInfo[FirebaseConstants.userIdField] = userId
        userInfo[FirebaseConstants.userNameField] = userName
        userInfo[FirebaseConstants.ageField] = age
        userInfo[FirebaseConstants.genderField] = gender
        userInfo[FirebaseConstants.countryField] = country
        userInfo[FirebaseConstants.cityField] = city

        saveUserInfo(userInfo, userId)
        saveAuthInfo(emailOrMobile = emailOrMobile, password = password, userId = userId)

        return userInfoResult && authResult
    }

    private fun getUserId() = UUID.randomUUID().toString().replace("-", "").toUpperCase()

    private suspend fun saveUserInfo(userInfo: HashMap<String, Any>, userId: String) {
        firebaseDb.collection(FirebaseConstants.userInfoTable).document(userId)
            .set(userInfo).addOnCompleteListener {
                if (it.isSuccessful) {
                    userInfoResult = true
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                userInfoResult = false
                Timber.e("failed")
            }.await()
    }

    private suspend fun saveAuthInfo(emailOrMobile: String, password: String, userId: String) {
        val authInfo = HashMap<String, Any>()
        authInfo[FirebaseConstants.emailOrMobileField] = emailOrMobile
        authInfo[FirebaseConstants.passwordField] = password
        authInfo[FirebaseConstants.userIdField] = userId

        firebaseDb.collection(FirebaseConstants.authInfoTable).document("$emailOrMobile")
            .set(authInfo)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    authResult = true
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                authResult = false
                Timber.e("failed")
            }.await()
    }
}