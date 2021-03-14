package com.hi.dear.data.model.common

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
open class UserCore(
        var id: String,
        var name: String,
        var picture: String? = null,
        var gender: String? = null
)