package com.hi.dear.source.remote


import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.hi.dear.source.INotificationDataSource
import com.hi.dear.ui.FirebaseConstants
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.fragment.notification.INotificationListener
import com.hi.dear.ui.fragment.notification.NotificationData
import timber.log.Timber


class FirebaseNotificationSource :
    INotificationDataSource {
    private var prefsManager = PrefsManager.getInstance()
    private var firebaseDb: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val mineId = prefsManager.readString(PrefsManager.UserId)

    private fun getNotificationDataFrom(snap: QueryDocumentSnapshot): NotificationData {
        val data = NotificationData()
        data.id = snap[FirebaseConstants.sender_id].toString()
        data.name = snap[FirebaseConstants.userNameField].toString()
        data.picture = snap[FirebaseConstants.pictureField].toString()
        data.notificationType = snap[FirebaseConstants.notification_type].toString()
        return data
    }

    override fun getNotifications(listener: INotificationListener) {
        var result = mutableListOf<NotificationData>()
        firebaseDb.collection(FirebaseConstants.notificationTable)
            .whereEqualTo(FirebaseConstants.receiver_id, mineId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Timber.e("listen:error")
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            result.add(getNotificationDataFrom(dc.document))
                            listener.incomingNotification(result)
                        }
                    }
                }
            }
    }
}