package com.hi.dear.ui.fragment.notification

interface INotificationListener {
    fun incomingNotification(messageList: MutableList<NotificationData>)
    fun inComingNotificationFailed()
}