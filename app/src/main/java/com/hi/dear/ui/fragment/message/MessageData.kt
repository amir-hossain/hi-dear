package com.hi.dear.ui.fragment.message

import com.hi.dear.data.model.common.UserCore

data class MessageData(
    val online_visibility: Boolean = false,
    val isFavourite: Boolean = false,
    var message: String? = null,
    val messageCount: Int? = null,
    val time: String? = null
) : UserCore()
