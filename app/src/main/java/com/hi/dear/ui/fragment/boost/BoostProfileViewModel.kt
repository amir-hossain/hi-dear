package com.hi.dear.ui.fragment.boost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.repo.BoostProfileRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import com.hi.dear.ui.fragment.top.TopProfileData
import kotlinx.coroutines.launch

class BoostProfileViewModel(private val repository: BoostProfileRepository) : BaseViewModel() {
    val boostProfileResult = MutableLiveData<ActionResult<Long>>()
    val deductCoinResult = MutableLiveData<ActionResult<Int>>()

    fun boostProfile(data: TopProfileData) {
        viewModelScope.launch {
            isLoading.value = true
            val requestResult = repository.boostProfile(data)
            if (requestResult is RawResult.Success) {
                boostProfileResult.value =
                    ActionResult(true, R.string.request_send, requestResult.data)
            } else if (requestResult is RawResult.Error) {
                boostProfileResult.value =
                    ActionResult(false, R.string.request_send_failed, null)
            }
            isLoading.value = false
        }
    }

    fun deductCoin(coin: Int,userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val requestResult = repository.deductCoin(coin,userId)
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
}