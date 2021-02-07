package com.hi.dear.data.state

/**
 * Data validation state of the login form.
 */
data class LoginFormState(val idError: Int? = null,
                          val passwordError: Int? = null,
                          val isDataValid: Boolean = false)