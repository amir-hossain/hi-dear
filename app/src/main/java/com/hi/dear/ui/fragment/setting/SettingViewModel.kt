package com.hi.dear.ui.fragment.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.repo.SettingRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel(private val repo: SettingRepository) : BaseViewModel() {
    val deleteResult = MutableLiveData<ActionResult<Boolean>>()
    fun deleteAccount() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repo.deleteAccount()
            if (result is RawResult.Success) {
                deleteResult.value = ActionResult(true, R.string.delete_successful, result.data)
            } else if (result is RawResult.Error) {
                deleteResult.value = ActionResult(false, R.string.delete_failed, null)
            }
            isLoading.value = false
        }
    }
}