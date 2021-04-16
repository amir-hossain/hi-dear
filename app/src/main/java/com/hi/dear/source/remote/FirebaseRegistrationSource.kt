package com.hi.dear.source.remote


import android.net.Uri
import com.google.common.io.Files.getFileExtension
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hi.dear.source.IRegistrationDataSource
import com.hi.dear.ui.FirebaseConstants
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.File
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
        password: String,
        picture: File
    ): Boolean {
        val userInfo = HashMap<String, Any>()
        val userId = getUserId()
        val pictureUrl = upload(picture)
        userInfo[FirebaseConstants.userIdField] = userId
        userInfo[FirebaseConstants.userNameField] = userName
        userInfo[FirebaseConstants.ageField] = age
        userInfo[FirebaseConstants.genderField] = gender
        userInfo[FirebaseConstants.countryField] = country
        userInfo[FirebaseConstants.cityField] = city
        userInfo[FirebaseConstants.pictureField] = pictureUrl
        upload(picture)
        saveUserInfo(userInfo, userId)
        saveAuthInfo(
            emailOrMobile = emailOrMobile,
            password = password,
            userId = userId,
            gender = gender,
            pictureUrl = pictureUrl,
            userName = userName
        )

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

    private suspend fun saveAuthInfo(
        emailOrMobile: String,
        password: String,
        userId: String,
        gender: String,
        pictureUrl: String,
        userName: String
    ) {
        val authInfo = HashMap<String, Any>()
        authInfo[FirebaseConstants.emailOrMobileField] = emailOrMobile
        authInfo[FirebaseConstants.passwordField] = password
        authInfo[FirebaseConstants.userIdField] = userId
        authInfo[FirebaseConstants.genderField] = gender
        authInfo[FirebaseConstants.pictureField] = pictureUrl
        authInfo[FirebaseConstants.userNameField] = userName

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

    private suspend fun upload(picture: File): String {
        var fileUrl = ""
        val fileRef = FirebaseStorage.getInstance()
            .getReference(System.currentTimeMillis().toString() + getFileExtension(picture.name))
        fileRef.putFile(Uri.fromFile(picture))
            .addOnCompleteListener {

            }.await()

        fileRef.downloadUrl.addOnSuccessListener { uri ->
            fileUrl = uri.toString()
        }.await()
        return fileUrl

    }
}