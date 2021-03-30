package com.hi.dear.ui.activity.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.Chat
import com.hi.dear.repo.ChatRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ChatViewModel(private val repo: ChatRepository) : BaseViewModel(), IChatListener {
    val incomingChatData = MutableLiveData<ActionResult<MutableList<Chat>>>()
    val chatSentResult = MutableLiveData<ActionResult<Boolean>>()

    fun sendMessage(text: String, otherUserId: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = repo.sendMessage(text, otherUserId)

            if (result is RawResult.Success) {
                chatSentResult.value =
                    ActionResult(true, R.string.chat_sent_success, result.data)
            } else {
                chatSentResult.value = ActionResult(false, R.string.chat_sent_failed, null)
            }
            isLoading.value = false
        }
    }

    fun getMessage(otherUserId: String) {
        isLoading.value = true
        repo.getMessage(otherUserId, this)
    }

    override fun incomingChat(messageList: MutableList<Chat>) {
        incomingChatData.value = ActionResult(true, R.string.registration_success, messageList)
        isLoading.value = false
    }

    override fun chatLoadFailed(messageList: MutableList<Chat>) {
        incomingChatData.value = ActionResult(false, R.string.registration_failed, null)
        isLoading.value = false
    }


}