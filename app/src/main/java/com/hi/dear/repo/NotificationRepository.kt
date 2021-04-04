package com.hi.dear.repo

import com.hi.dear.source.INotificationDataSource
import com.hi.dear.ui.fragment.notification.INotificationListener

class NotificationRepository(private val dataSource: INotificationDataSource) : IRepository {

    fun getNotifications(listener: INotificationListener) {
        dataSource.getNotifications(listener)
    }
}