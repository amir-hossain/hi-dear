package com.hi.dear.data.model.common


/**
 * Set messageId for recycler view adapter
 * Firebase timestamp is created after init method.
 */

data class Chat(
    var id: String? = null,
    var senderId: String? = null,
    var receiverId: String? = null,
    var isOwner: Boolean = false,
    var name: String? = null,
    var photoUrl: String? = null,
    var text: String? = null,
    var timestamp: Any? = null,
    var readTimestamp: Any? = null
) {
/*    @get:Exclude
    var audioDownloaded = false*/

    fun setMessageId() {
        val timestamp = this.timestamp
        /*if (timestamp is Timestamp) {
            id = senderId + "_" + timestamp.toDate().time
        }*/
    }
}