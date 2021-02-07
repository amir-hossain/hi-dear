package com.hi.dear.ui.activity.register

data class ActionResult<T>(
        val success: Boolean,
        val msg: Int,
        val data:T?
)