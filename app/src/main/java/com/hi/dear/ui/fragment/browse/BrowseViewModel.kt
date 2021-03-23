package com.hi.dear.ui.fragment.browse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.repo.BrowseRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class BrowseViewModel(private val repo: BrowseRepository) : BaseViewModel() {

    val result = MutableLiveData<ActionResult<MutableList<UserCore>>>()

    fun getBrowseData(gender: String, limit: Long) {
        viewModelScope.launch {
            isLoading.value = true
            val browseData = repo.getBrowseData(gender, limit)
            if (browseData is RawResult.Success) {
                result.value = ActionResult(true, R.string.login_successful, browseData.data)
            } else if (browseData is RawResult.Error) {
                result.value = ActionResult(false, R.string.login_failed, null)
            }
            isLoading.value = false
        }
    }
}