package com.hi.dear.ui.fragment.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.repo.RequestRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class RequestViewModel(private val repository: RequestRepository) : BaseViewModel() {

    val requestResult = MutableLiveData<ActionResult<MutableList<RequestData>>>()
    val requestReactResult = MutableLiveData<ActionResult<RequestData>>()

    fun getRequest() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getRequestData()
            if (result is RawResult.Success) {
                requestResult.value = ActionResult(true, R.string.fetch_success, result.data)
            } else {
                requestResult.value = ActionResult(false, R.string.fetch_failed, null)
            }
            isLoading.value = false
        }
    }

    fun reactToRequest(isAccepted: Boolean, requestData: RequestData) {
        var successMsgId = R.string.message_declined
        var failedMsgId = R.string.message_declined_failed
        if (isAccepted) {
            successMsgId = R.string.message_accepted
            failedMsgId = R.string.message_accepted_failed
        }
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.reactToRequest(isAccepted, requestData)
            if (result is RawResult.Success) {
                requestReactResult.value = ActionResult(true, successMsgId, result.data)
            } else {
                requestReactResult.value = ActionResult(false, failedMsgId, null)
            }
            isLoading.value = false
        }
    }
}