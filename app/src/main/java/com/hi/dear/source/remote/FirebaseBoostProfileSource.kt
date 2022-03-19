package com.hi.dear.source.remote


import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.IBoostProfileDataSource
import com.hi.dear.ui.Constant
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
        val map = HashMap<String, Any>()
        map[userIdField] = data.id!!
        map[userNameField] = data.name!!
        map[genderField] = data.gender!!
        map[pictureField] = data.picture!!
        map[endTimeField] = data.endTime!!
        return map
    }

    override suspend fun deductCoin(coin: Int, userId: String): Int {
        var result = Constant.CurrentCoin
        val newCoin = Constant.CurrentCoin - coin
        val ref = firebaseDb.collection(FirebaseConstants.coinTable).document(userId)
        ref.update(FirebaseConstants.coinField, newCoin)
            .addOnSuccessListener { result = newCoin }.await()
        return result
    }

    override suspend fun getBoostEndTime(userId: String): Long {
        var result = 0L
        firebaseDb.collection(FirebaseConstants.boostedTable).document(userId)
            .get()
            .addOnSuccessListener {
                if (it.data != null) {
                    result = it.data!![endTimeField].toString().toLong()
                }
            }.await()
        return result
    }
}