package com.hi.dear.ui.activity.match

import android.content.Intent
import android.os.Bundle
import com.hi.dear.data.model.common.Chat
import com.hi.dear.databinding.ActivityMatchBinding
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.base.BaseActivity


class MatchActivity : BaseActivity() {

    private lateinit var binding: ActivityMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarLayout.toolbarTitle.text = "Match"
    }
}