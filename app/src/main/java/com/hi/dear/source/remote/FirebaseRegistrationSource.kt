package com.hi.dear.source.remote


import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.hi.dear.source.IRegistrationDataSource
import com.hi.dear.ui.Utils
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class FirebaseRegistrationSource :
    IRegistrationDataSource {
    private var firebaseDb: CollectionReference =
        FirebaseFirestore.getInstance().collection("userInfo")

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
        userInfo["emailOrMobile"] = emailOrMobile
        userInfo["city"] = city
        userInfo["password"] = password

        var result = false

        firebaseDb.document().set(userInfo).addOnCompleteListener {
            if (it.isSuccessful) {
                result = true
                Timber.i(Utils.formatLogMessage("register", "isSuccessful", it.isSuccessful))
            }
        }.addOnFailureListener {
            result = true
            Timber.i(
                Utils.formatLogMessage(
                    "register",
                    "addOnFailureListener",
                    it.localizedMessage
                )
            )
        }.await()

        return result
    }

}