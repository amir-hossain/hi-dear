package com.hi.dear.data.model.common

data class ProfileData(
    var country: String? = null,
    var city: String? = null,
    var age: String? = null,
    var about: String? = null
) : UserCore()