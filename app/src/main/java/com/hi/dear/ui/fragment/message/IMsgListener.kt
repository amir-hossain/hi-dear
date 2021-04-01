package com.hi.dear.ui.fragment.message

interface IMsgListener {
    fun incomingMsg(messageList: MutableList<MessageData>)
    fun inComingMsgFailed()
}