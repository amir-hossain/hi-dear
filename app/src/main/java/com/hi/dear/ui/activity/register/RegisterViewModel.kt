package com.hi.dear.ui.activity.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.state.RegistrationFormState
import com.hi.dear.repo.RegistrationRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File

class RegisterViewModel(private val repo: RegistrationRepository) : BaseViewModel() {

    val registrationFormState = MutableLiveData<RegistrationFormState>()
    val registrationResult = MutableLiveData<ActionResult<Void>>()

    fun register(
        userName: String, age: String, gender: String, password: String, county: String,
        city: String, emailOrMobile: String, picture: File
    ) {

        viewModelScope.launch {
            isLoading.value = true
            val result = repo.register(
                userName = userName,
                age = age,
                gender = gender,
                country = county,
                city = city,
                password = password,
                emailOrMobile = emailOrMobile,
                picture = picture
            )

            if (result is RawResult.Success) {
                registrationResult.value = ActionResult(true, R.string.registration_success, null)
            } else {
                registrationResult.value = ActionResult(false, R.string.registration_failed, null)
            }
            isLoading.value = false
        }
    }

    fun registerDataChanged(
        userName: String, age: String, password: String,
        gender: String, emailOrMobile: String, city: String, country: String, picture: File?
    ) {
        if (picture == null) {
            registrationFormState.value =
                RegistrationFormState(pictureError = R.string.invalid_picture)
        } else if (userName.isBlank()) {
            registrationFormState.value =
                RegistrationFormState(userNameError = R.string.user_name_empty)
        } else if (age.isBlank()) {
            registrationFormState.value = RegistrationFormState(ageError = R.string.age_empty)
        } else if (gender.isBlank()) {
            registrationFormState.value = RegistrationFormState(genderError = R.string.gender_empty)
        } else if (country.isBlank()) {
            registrationFormState.value =
                RegistrationFormState(countryError = R.string.country_empty)
        } else if (city.isBlank()) {
            registrationFormState.value = RegistrationFormState(cityError = R.string.city_empty)
        } else if (emailOrMobile.isBlank()) {
            registrationFormState.value =
                RegistrationFormState(emailOrMobileError = R.string.email_mobile_empty)
        } else if (!isPasswordValid(password)) {
            registrationFormState.value =
                RegistrationFormState(passwordError = R.string.invalid_password)
        } else {
            registrationFormState.value = RegistrationFormState(isDataValid = true)
        }
    }


    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}