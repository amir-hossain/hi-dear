package com.hi.dear.ui.fragment.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hi.dear.R
import com.hi.dear.repo.MessageRepository
import com.hi.dear.ui.activity.ActionResult

class NotificationViewModel(private val repository: MessageRepository) : ViewModel(),
    INotificationListener {

    val liveResult = MutableLiveData<ActionResult<MutableList<NotificationData>>>()

    fun getMessage() {

    }

    override fun incomingNotification(messageList: MutableList<NotificationData>) {
        liveResult.value = ActionResult(true, R.string.registration_success, messageList)
    }

    override fun inComingNotificationFailed() {
        liveResult.value = ActionResult(false, R.string.registration_failed, null)
    }
}