package com.hi.dear.ui.activity.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.state.RegistrationFormState
import com.hi.dear.repo.RegistrationRepository

class RegisterViewModel(private val registrationRepository: RegistrationRepository) : ViewModel() {

    val registrationFormState = MutableLiveData<RegistrationFormState>()


    val registrationResult = MutableLiveData<ActionResult<Void>>()

    fun register(id: String, name:String,photo:String,password: String,conPass:String) {
        val result = registrationRepository.register(id, name,photo,password,conPass)

        if (result is RawResult.Success) {
            registrationResult.value = ActionResult(true, R.string.registration_success, null)
        } else {
            registrationResult.value = ActionResult(false,R.string.registration_failed,null)
        }
    }

    fun registerDataChanged(id: String, name:String, password: String, conPass: String) {
        if (!isIdValid(id)) {
            registrationFormState.value = RegistrationFormState(idError = R.string.invalid_id)
        } else if (!isIdValid(name)) {
            registrationFormState.value = RegistrationFormState(nameError = R.string.invalid_name)
        } else if (!isPasswordValid(password)) {
            registrationFormState.value = RegistrationFormState(passwordError = R.string.invalid_password)
        } else if (!isConPassValid(password,conPass)) {
            registrationFormState.value = RegistrationFormState(conPassError = R.string.invalid_con_pass)
        } else {
            registrationFormState.value = RegistrationFormState(isDataValid = true)
        }
    }

    private fun isConPassValid(pass:String,conPass: String): Boolean {
        return pass == conPass
    }

    // A placeholder other value validation check
    private fun isIdValid(value: String): Boolean {
        return !value.isBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}