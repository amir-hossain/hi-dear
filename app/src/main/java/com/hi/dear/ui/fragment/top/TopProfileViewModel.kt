package com.hi.dear.ui.fragment.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.repo.TopProfileRepository
import com.hi.dear.ui.App
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class TopProfileViewModel(private val repository: TopProfileRepository) : BaseViewModel() {

    val requestResult = MutableLiveData<ActionResult<MutableList<TopProfileData>>>()

    fun getTopProfile() {
        if (!Utils.isConnected(App.instance)) {
            isConnected.value = false
            return
        }
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getTopProfile()
            if (result is RawResult.Success) {
                requestResult.value = ActionResult(true, R.string.fetch_success, result.data)
            } else {
                requestResult.value = ActionResult(false, R.string.fetch_failed, null)
            }
            isLoading.value = false
        }
    }
}