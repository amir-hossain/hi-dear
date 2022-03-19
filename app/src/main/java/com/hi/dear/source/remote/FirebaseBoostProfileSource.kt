package com.hi.dear.source.remote


import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.IBoostProfileDataSource
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.FirebaseConstants.emailOrMobileField
import com.hi.dear.ui.FirebaseConstants.genderField
import com.hi.dear.ui.FirebaseConstants.pictureField
import com.hi.dear.ui.FirebaseConstants.endTimeField
import com.hi.dear.ui.FirebaseConstants.userIdField
import com.hi.dear.ui.FirebaseConstants.userNameField
import com.hi.dear.ui.fragment.top.TopProfileData
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseBoostProfileSource : IBoostProfileDataSource {
    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun boostProfile(data: TopProfileData): Long {
        var result = 0L
        firebaseDb.collection(FirebaseConstants.boostedTable)
            .document(data.id!!)
            .set(getHashMapFrom(data))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result = data.endTime
                    Timber.i("isSuccessful")
                }
            }.addOnFailureListener {
                Timber.e("failed")
            }.await()
        return result
    }

    private fun getHashMapFrom(data: TopProfileData): HashMap<String, Any> {
        return hashMapOf(
            userIdField to {data.id},
            userNameField to {data.name},
            pictureField to {data.picture},
            emailOrMobileField to {data.emailOrMobile},
            genderField to {data.gender},
            endTimeField to {data.endTime}
        )
    }
}