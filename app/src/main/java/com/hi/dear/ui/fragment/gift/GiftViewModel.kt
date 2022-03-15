package com.hi.dear.ui.fragment.gift

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.repo.GiftRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class GiftViewModel(private val repository: GiftRepository) : BaseViewModel() {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val giftAvailableResult = MutableLiveData<ActionResult<Boolean>>()
    val setLastTimeResult = MutableLiveData<ActionResult<Boolean>>()
    val giftCoinResult = MutableLiveData<ActionResult<Int>>()

    fun isGiftAvailable(userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getLastOpenDate(userId)
            if (result is RawResult.Success) {
                if (result.data.isBlank()) {
                    giftAvailableResult.value = ActionResult(true, R.string.fetch_success, true)
                } else if (lessThenToday(getDateFrom(result.data))) {
                    giftAvailableResult.value = ActionResult(true, R.string.fetch_success, true)
                } else {
                    giftAvailableResult.value = ActionResult(true, R.string.fetch_success, false)
                }
            } else {
                giftAvailableResult.value = ActionResult(false, R.string.fetch_failed, null)
            }
            isLoading.value = false
        }
    }

    private fun getDateFrom(data: String): Date {
        return dateFormat.parse(data)
    }

    private fun lessThenToday(lastOpenDate: Date): Boolean {
        val todayString = dateFormat.format(Date())
        return lastOpenDate < dateFormat.parse(todayString)
    }

    fun setLastOpenDate(userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.setLastOpenDate(userId, dateFormat.format(Date()))
            if (result is RawResult.Success) {
                setLastTimeResult.value = ActionResult(true, R.string.fetch_success, result.data)
            } else {
                setLastTimeResult.value = ActionResult(false, R.string.fetch_failed, null)
            }
            isLoading.value = false
        }
    }

    fun giftCoin(giftCoint: Int, userId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val requestResult = repository.giftCoin(giftCoint,userId)
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