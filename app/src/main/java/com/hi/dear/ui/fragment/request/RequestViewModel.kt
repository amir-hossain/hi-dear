package com.hi.dear.ui.fragment.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.repo.MatchRepository
import com.hi.dear.ui.activity.ActionResult

class RequestViewModel(private val repository: MatchRepository) : ViewModel() {

    val liveResult = MutableLiveData<ActionResult<MutableList<RequestData>>>()

    fun getMatch() {
        val result = repository.getMatchData()
        if (result is RawResult.Success) {
            liveResult.value = ActionResult(true, R.string.registration_success, result.data)
        } else {
            liveResult.value = ActionResult(false, R.string.registration_failed, null)
        }
    }
}