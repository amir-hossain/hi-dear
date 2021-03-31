package com.hi.dear.ui.fragment.message

data class MessageData(
    var image: String? = null,
    val online_visibility: Boolean = false,
    val isFavourite: Boolean = false,
    var name: String? = null,
    var message: String? = null,
    val messageCount: Int? = null,
    val time: String? = null
) {
    var isClicked = false
}
