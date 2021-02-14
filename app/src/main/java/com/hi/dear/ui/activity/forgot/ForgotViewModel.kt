package com.hi.dear.ui.activity.forgot

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hi.dear.R
import com.hi.dear.data.state.ForgetPassFormState
import com.hi.dear.repo.ForgetPasswordRepository
import com.hi.dear.ui.activity.ActionResult

class ForgotViewModel(private val forgetRepository: ForgetPasswordRepository) : ViewModel() {

    val forgetPassFormState = MutableLiveData<ForgetPassFormState>()

    val forgetPassResult = MutableLiveData<ActionResult<Boolean>>()

    fun send(email: String) {
        forgetPassResult.value = ActionResult(true, R.string.forget_pass_send_success, null)
    }

    fun forgetPassDataChanged(email: String) {
        if (email.isBlank()) {
            forgetPassFormState.value = ForgetPassFormState(emailError = R.string.email_empty)
        } else {
            forgetPassFormState.value = ForgetPassFormState(isDataValid = true)
        }
    }
}