package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.source.IProfileDataSource
import com.hi.dear.ui.FirebaseConstants
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseProfileSource : IProfileDataSource {
    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun getProfile(userId: String): ProfileData {
        var userData: ProfileData? = null
        firebaseDb.collection(FirebaseConstants.userInfoTable)
            .whereEqualTo(FirebaseConstants.userIdField, userId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && it.result!!.documents != null) {
                    userData = parseUserFrom(it.result!!.documents)
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return userData!!
    }

    private fun parseUserFrom(resultList: MutableList<DocumentSnapshot>): ProfileData {
        val profileData = ProfileData()
        for (result in resultList) {
            profileData.id = result[FirebaseConstants.userIdField].toString()
            profileData.name = result[FirebaseConstants.userNameField].toString()
            profileData.gender = result[FirebaseConstants.genderField].toString()
            profileData.picture = result[FirebaseConstants.pictureField].toString()
            profileData.country = result[FirebaseConstants.countryField].toString()
            profileData.city = result[FirebaseConstants.cityField].toString()
            profileData.age = result[FirebaseConstants.ageField].toString()
            profileData.about = result[FirebaseConstants.aboutField]?.toString() ?: ""
            break
        }
        return profileData
    }
}