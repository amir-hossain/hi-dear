package com.hi.dear.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.repo.*
import com.hi.dear.ui.activity.chat.ChatViewModel
import com.hi.dear.ui.activity.forgetPass.ForgotViewModel
import com.hi.dear.ui.activity.login.LoginViewModel
import com.hi.dear.ui.activity.message.MessageViewModel
import com.hi.dear.ui.activity.profile.ProfileViewModel
import com.hi.dear.ui.activity.register.RegisterViewModel
import com.hi.dear.ui.fragment.boost.BoostProfileViewModel
import com.hi.dear.ui.fragment.browse.BrowseViewModel
import com.hi.dear.ui.fragment.gift.GiftViewModel
import com.hi.dear.ui.fragment.notification.NotificationViewModel
import com.hi.dear.ui.fragment.match.RequestViewModel
import com.hi.dear.ui.fragment.setting.SettingViewModel
import com.hi.dear.ui.fragment.top.TopProfileViewModel

class ViewModelFactory(private val repository: IRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (repository) {
            is LoginRepository -> {
                return LoginViewModel(repository) as T
            }
            is RegistrationRepository -> {
                return RegisterViewModel(repository) as T
            }
            is ForgetPasswordRepository -> {
                return ForgotViewModel(repository) as T
            }
            is MessageRepository -> {
                return MessageViewModel(repository) as T
            }
            is RequestRepository -> {
                return RequestViewModel(repository) as T
            }
            is BrowseRepository -> {
                return BrowseViewModel(repository) as T
            }
            is ChatRepository -> {
                return ChatViewModel(repository) as T
            }
            is NotificationRepository -> {
                return NotificationViewModel(repository) as T
            }
            is ProfileRepository -> {
                return ProfileViewModel(repository) as T
            }
            is SettingRepository -> {
                return SettingViewModel(repository) as T
            }
            is TopProfileRepository -> {
                return TopProfileViewModel(repository) as T
            }
            is GiftRepository -> {
                return GiftViewModel(repository) as T
            }
            is BoostProfileRepository -> {
                return BoostProfileViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}