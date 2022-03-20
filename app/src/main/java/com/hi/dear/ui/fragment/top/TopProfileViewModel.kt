package com.hi.dear.ui.fragment.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.repo.TopProfileRepository
import com.hi.dear.ui.App
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class TopProfileViewModel(private val repository: TopProfileRepository) : BaseViewModel() {

    val topProfileResult = MutableLiveData<ActionResult<MutableList<TopProfileData>>>()

    fun getTopProfile() {
        if (!Utils.isConnected(App.instance)) {
            isConnected.value = false
            return
        }
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getTopProfile()
            if (result is RawResult.Success) {
                topProfileResult.value =
                    ActionResult(true, R.string.fetch_success, getFilteredData(result.data))
            } else {
                topProfileResult.value = ActionResult(false, R.string.fetch_failed, null)
            }
            isLoading.value = false
        }
    }

    private fun getFilteredData(dataList: MutableList<TopProfileData>): MutableList<TopProfileData> {
        val list = mutableListOf<TopProfileData>()
        for (data in dataList) {
            if (timeIsNotExpired(data.endTime)) {
                list.add(data)
            }
        }
        return list
    }

    private fun timeIsNotExpired(endTime: Long): Boolean {
        return endTime > System.currentTimeMillis()
    }
}