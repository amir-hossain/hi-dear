package com.hi.dear.source

import com.hi.dear.ui.fragment.notification.INotificationListener

interface INotificationDataSource {
    fun getNotifications(listener: INotificationListener)
}
