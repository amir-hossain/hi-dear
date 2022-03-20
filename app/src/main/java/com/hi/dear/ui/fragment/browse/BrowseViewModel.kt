package com.hi.dear.ui.fragment.browse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.repo.BrowseRepository
import com.hi.dear.ui.App
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class BrowseViewModel(private val repo: BrowseRepository) : BaseViewModel() {

    val browseDataResult = MutableLiveData<ActionResult<MutableList<UserCore>>>()
    val requestDataResult = MutableLiveData<ActionResult<Boolean>>()
    val remainingCoinDataResult = MutableLiveData<ActionResult<Int>>()
    val deductCoinResult = MutableLiveData<ActionResult<Int>>()
    val giftCoinResult = MutableLiveData<ActionResult<Int>>()

    // used to pass data in browse fragment
    val remainingCoin = MutableLiveData<Int>()
    val adButtonVisibility = MutableLiveData<Boolean>()

    fun getBrowseData(gender: String, limit: Long) {
        if (!Utils.isConnected(App.instance)) {
            isConnected.value = false
            return
        }
        viewModelScope.launch {
            isLoading.value = true
            val browseData = repo.getBrowseData(gender, limit)
            if (browseData is RawResult.Success) {
                browseDataResult.value =
                    ActionResult(true, R.string.login_successful, browseData.data)
            } else if (browseData is RawResult.Error) {
                browseDataResult.value = ActionResult(false, R.string.login_failed, null)
            }
            isLoading.value = false
        }
    }

    fun sendRequest(receiverUserData: UserCore) {
        if (!Utils.isConnected(App.instance)) {
            isConnected.value = false
            return
        }
        viewModelScope.launch {
            isLoading.value = true
            val requestResult = repo.sendRequest(receiverUserData)
            if (requestResult is RawResult.Success) {
                requestDataResult.value =
                    ActionResult(true, R.string.request_send, requestResult.data)
            } else if (requestResult is RawResult.Error) {
                requestDataResult.value = ActionResult(false, R.string.request_send_failed, null)
            }
            isLoading.value = false
        }
    }

    fun getRemainingCoin(userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val requestResult = repo.getRemainingCoin(userId)
            if (requestResult is RawResult.Success) {
                remainingCoinDataResult.value =
                    ActionResult(true, R.string.request_send, requestResult.data)
            } else if (requestResult is RawResult.Error) {
                remainingCoinDataResult.value =
                    ActionResult(false, R.string.request_send_failed, null)
            }
            isLoading.value = false
        }
    }

    fun deductCoin(coinOfRequest: Int,userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val requestResult = repo.deductCoin(coinOfRequest,userId)
            if (requestResult is RawResult.Success) {
                deductCoinResult.value =
                    ActionResult(true, R.string.request_send, requestResult.data)
            } else if (requestResult is RawResult.Error) {
                deductCoinResult.value =
                    ActionResult(false, R.string.request_send_failed, null)
            }
            isLoading.value = false
        }
    }

    fun giftCoin(giftCoint: Int, userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val requestResult = repo.giftCoin(giftCoint,userId)
            if (requestResult is RawResult.Success) {
                giftCoinResult.value =
                    ActionResult(true, R.string.request_send, requestResult.data)
            } else if (requestResult is RawResult.Error) {
                giftCoinResult.value =
                    ActionResult(false, R.string.request_send_failed, null)
            }
            isLoading.value = false
        }
    }
}