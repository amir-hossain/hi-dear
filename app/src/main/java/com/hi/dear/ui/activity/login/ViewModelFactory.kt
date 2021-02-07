package com.hi.dear.ui.activity.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.repo.IRepository
import com.hi.dear.repo.LoginRepository
import com.hi.dear.repo.RegistrationRepository
import com.hi.dear.ui.activity.register.RegisterViewModel

class ViewModelFactory(private val repository: IRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (repository is LoginRepository) {
            return LoginViewModel(repository) as T
        } else if (repository is RegistrationRepository) {
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}