package com.hi.dear.data.model.common

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
open class UserCore(
    var id: String? = null,
    var name: String? = null,
    var picture: String? = null,
    var gender: String? = null
)