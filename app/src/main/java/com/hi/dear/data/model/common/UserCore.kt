package com.hi.dear.data.model.common

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class UserCore(
        val id: String,
        val name: String
)