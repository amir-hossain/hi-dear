package com.hi.dear.ui.activity.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.repo.ProfileRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: ProfileRepository) : BaseViewModel() {

    val profileResult = MutableLiveData<ActionResult<ProfileData>>()

    fun getProfileData(userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val browseData = repo.getProfileData(userId)
            if (browseData is RawResult.Success) {
                profileResult.value =
                    ActionResult(true, R.string.fetch_success, browseData.data)
            } else if (browseData is RawResult.Error) {
                profileResult.value = ActionResult(false, R.string.fetch_failed, null)
            }
            isLoading.value = false
        }
    }
}