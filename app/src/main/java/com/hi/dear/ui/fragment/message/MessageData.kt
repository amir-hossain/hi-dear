package com.hi.dear.ui.fragment.message

data class MessageData(
    val image: String? = null,
    val online_visibility: Boolean = false,
    val isFavourite: Boolean = false,
    val name: String? = null,
    val message: String? = null,
    val messageCount: Int? = null,
    val time: String? = null
) {
    var isClicked = false
}
