package com.hi.dear.ui.activity.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hi.dear.R
import com.hi.dear.repo.MessageRepository
import com.hi.dear.ui.activity.ActionResult

class MessageViewModel(private val repository: MessageRepository) : ViewModel(), IMsgListener {

    val liveResult = MutableLiveData<ActionResult<MutableList<MessageData>>>()

    fun getMessage() {
        repository.getMessageData(this)
    }

    override fun incomingMsg(messageList: MutableList<MessageData>) {
        liveResult.value = ActionResult(true, R.string.registration_success, messageList)
    }

    override fun inComingMsgFailed() {
        liveResult.value = ActionResult(false, R.string.registration_failed, null)
    }
}