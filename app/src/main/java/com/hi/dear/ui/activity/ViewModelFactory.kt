package com.hi.dear.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.repo.*
import com.hi.dear.ui.activity.forgot.ForgotViewModel
import com.hi.dear.ui.activity.login.LoginViewModel
import com.hi.dear.ui.activity.register.RegisterViewModel
import com.hi.dear.ui.fragment.browse.BrowseViewModel
import com.hi.dear.ui.fragment.message.MessageViewModel
import com.hi.dear.ui.fragment.request.RequestViewModel

class ViewModelFactory(private val repository: IRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (repository) {
            is LoginRepository -> {
                return LoginViewModel(repository) as T
            }
            is IRegistrationRepository -> {
                return RegisterViewModel(repository) as T
            }
            is ForgetPasswordRepository -> {
                return ForgotViewModel(repository) as T
            }
            is MessageRepository -> {
                return MessageViewModel(repository) as T
            }
            is MatchRepository -> {
                return RequestViewModel(repository) as T
            }
            is BrowseRepository -> {
                return BrowseViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}