package com.hi.dear.data.state

/**
 * Data validation state of the login form.
 */
data class RegistrationFormState(
    val userNameError: Int? = null,
    val passwordError: Int? = null,
    val ageError: Int? = null,
    val genderError: Int? = null,
    val emailOrMobileError: Int? = null,
    val cityError: Int? = null,
    val countryError: Int? = null,
    val pictureError: Int? = null,
    val isDataValid: Boolean = false
)