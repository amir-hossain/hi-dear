package com.hi.dear.ui.activity.message

interface IMsgListener {
    fun incomingMsg(messageList: MutableList<MessageData>)
    fun inComingMsgFailed()
}