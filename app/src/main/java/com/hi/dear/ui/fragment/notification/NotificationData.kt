package com.hi.dear.ui.fragment.notification

import com.hi.dear.data.model.common.UserCore

data class NotificationData(
    var notificationType: String? = null,
    val time: String? = null
) : UserCore()
