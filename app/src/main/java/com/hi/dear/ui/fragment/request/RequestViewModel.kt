package com.hi.dear.ui.fragment.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.repo.RequestRepository
import com.hi.dear.ui.activity.ActionResult
import kotlinx.coroutines.launch

class RequestViewModel(private val repository: RequestRepository) : ViewModel() {

    val requestResult = MutableLiveData<ActionResult<MutableList<RequestData>>>()

    fun getRequest() {
        viewModelScope.launch {
            val result = repository.getRequestData()
            if (result is RawResult.Success) {
                requestResult.value = ActionResult(true, R.string.fetch_success, result.data)
            } else {
                requestResult.value = ActionResult(false, R.string.fetch_failed, null)
            }
        }
    }
}