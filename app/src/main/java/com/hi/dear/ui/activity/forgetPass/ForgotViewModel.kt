package com.hi.dear.ui.activity.forgetPass

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.state.ForgetPassFormState
import com.hi.dear.repo.ForgetPasswordRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ForgotViewModel(private val repo: ForgetPasswordRepository) : BaseViewModel() {

    val forgetPassFormState = MutableLiveData<ForgetPassFormState>()

    val forgetPassResult = MutableLiveData<ActionResult<String>>()

    fun send(emailOrMobile: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repo.forgetPassword(emailOrMobile)
            if (result is RawResult.Success) {
                forgetPassResult.value = ActionResult(true, R.string.password_fetched, result.data)
            } else if (result is RawResult.Error) {
                forgetPassResult.value = ActionResult(false, R.string.user_not_found, null)
            }
            isLoading.value = false
        }
    }

    fun forgetPassDataChanged(email: String) {
        if (email.isBlank()) {
            forgetPassFormState.value = ForgetPassFormState(emailError = R.string.email_empty)
        } else {
            forgetPassFormState.value = ForgetPassFormState(isDataValid = true)
        }
    }
}