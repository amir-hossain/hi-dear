package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.source.IProfileDataSource
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.PrefsManager
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

    override suspend fun saveEditedData(
        newName: String?,
        newAge: String?,
        newCountry: String?,
        newCity: String?,
        newGender: String?,
        newAbout: String?
    ): Boolean {
        var result = false
        val myId = PrefsManager.getInstance().readString(PrefsManager.UserId)!!
        firebaseDb.collection(FirebaseConstants.userInfoTable)
            .document(myId)
            .update(getEditHashMap(newName, newAge, newCountry, newCity, newGender, newAbout))
            .addOnSuccessListener {
                result = true
            }.await()
        return result
    }

    private fun parseUserFrom(resultList: MutableList<DocumentSnapshot>): ProfileData {
        val profileData = ProfileData()
        for (result in resultList) {
            profileData.id = result[FirebaseConstants.userIdField].toString()
            profileData.name = result[FirebaseConstants.userNameField].toString()
            profileData.gender = result[FirebaseConstants.genderField].toString()
            profileData.country = result[FirebaseConstants.countryField].toString()
            profileData.city = result[FirebaseConstants.cityField].toString()
            profileData.age = result[FirebaseConstants.ageField].toString()
            profileData.about = result[FirebaseConstants.aboutField]?.toString() ?: ""
            profileData.setPicList(result[FirebaseConstants.pictureField].toString())
            break
        }
        return profileData
    }

    private fun getEditHashMap(
        newName: String?,
        newAge: String?,
        newCountry: String?,
        newCity: String?,
        newGender: String?,
        newAbout: String?
    ): HashMap<String, Any> {
        val updateInfo = HashMap<String, Any>()
        if (newName != null) {
            updateInfo[FirebaseConstants.userNameField] = newName
        }

        if (newAge != null) {
            updateInfo[FirebaseConstants.ageField] = newAge
        }

        if (newCountry != null) {
            updateInfo[FirebaseConstants.countryField] = newCountry
        }

        if (newCity != null) {
            updateInfo[FirebaseConstants.cityField] = newCity
        }

        if (newGender != null) {
            updateInfo[FirebaseConstants.genderField] = newGender
        }

        if (newAbout != null) {
            updateInfo[FirebaseConstants.aboutField] = newAbout
        }
        return updateInfo
    }
}