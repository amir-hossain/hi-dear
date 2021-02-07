package com.hi.dear.ui.activity.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.data.state.LoginFormState
import com.hi.dear.repo.LoginRepository
import com.hi.dear.ui.activity.register.ActionResult

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    val loginFormState = MutableLiveData<LoginFormState>()


    val loginResult = MutableLiveData<ActionResult<UserCore>>()

    fun login(id: String, password: String) {
        val result = loginRepository.login(id, password)

        if (result is RawResult.Success) {
            loginResult.value = ActionResult(true, R.string.login_successful, result.data)
        } else if(result is RawResult.Error){
            loginResult.value = ActionResult(false,R.string.login_failed,null)
        }
    }

    fun loginDataChanged(id: String, password: String) {
        if (!isIdValid(id)) {
            loginFormState.value = LoginFormState(idError = R.string.invalid_id)
        } else if (!isPasswordValid(password)) {
            loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder id validation check
    private fun isIdValid(id: String): Boolean {
        return id.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}