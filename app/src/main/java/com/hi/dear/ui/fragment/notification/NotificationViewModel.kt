package com.hi.dear.ui.fragment.notification

import androidx.lifecycle.MutableLiveData
import com.hi.dear.R
import com.hi.dear.repo.NotificationRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel

class NotificationViewModel(private val repository: NotificationRepository) : BaseViewModel(),
    INotificationListener {

    val liveResult = MutableLiveData<ActionResult<MutableList<NotificationData>>>()

    fun getNotifications() {
        isLoading.value = true
        repository.getNotifications(this)
    }

    override fun incomingNotification(notificationList: MutableList<NotificationData>) {
        isLoading.value = false
        liveResult.value = ActionResult(true, R.string.registration_success, notificationList)
    }

    override fun inComingNotificationFailed() {
        isLoading.value = false
        liveResult.value = ActionResult(false, R.string.registration_failed, null)
    }
}