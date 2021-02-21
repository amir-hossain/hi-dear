package com.hi.dear.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.repo.*
import com.hi.dear.ui.activity.forgot.ForgotViewModel
import com.hi.dear.ui.activity.login.LoginViewModel
import com.hi.dear.ui.activity.register.RegisterViewModel
import com.hi.dear.ui.fragment.match.MatchViewModel
import com.hi.dear.ui.fragment.message.MessageViewModel

class ViewModelFactory(private val repository: IRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (repository is LoginRepository) {
            return LoginViewModel(repository) as T
        } else if (repository is RegistrationRepository) {
            return RegisterViewModel(repository) as T
        } else if (repository is ForgetPasswordRepository) {
            return ForgotViewModel(repository) as T
        } else if (repository is MessageRepository) {
            return MessageViewModel(repository) as T
        } else if (repository is MatchRepository) {
            return MatchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}