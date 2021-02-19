package com.hi.dear.ui.fragment.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.repo.MessageRepository
import com.hi.dear.ui.activity.ActionResult

class MessageViewModel(private val repository: MessageRepository) : ViewModel() {

    val liveResult = MutableLiveData<ActionResult<List<MessageData>>>()

    fun getMessage() {
        val result = repository.getMessageData()
        if (result is RawResult.Success) {
            liveResult.value = ActionResult(true, R.string.registration_success, result.data)
        } else {
            liveResult.value = ActionResult(false, R.string.registration_failed, null)
        }
    }
}