package com.hi.dear.data.model.common


/**
 * Set messageId for recycler view adapter
 * Firebase timestamp is created after init method.
 */

data class Chat(
    var id: String? = null,
    var senderId: String? = null,
    var name: String? = null,
    var photoUrl: String? = null,
    var text: String? = null,
    var timestamp: String? = null,
    var readTimestamp: String? = null
)