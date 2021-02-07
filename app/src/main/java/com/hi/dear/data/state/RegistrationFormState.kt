package com.hi.dear.data.state

/**
 * Data validation state of the login form.
 */
data class RegistrationFormState(val idError: Int? = null,
                                 val passwordError: Int? = null,
                                 val conPassError: Int?= null,
                                 val nameError: Int? = null,
                                 val isDataValid: Boolean = false)