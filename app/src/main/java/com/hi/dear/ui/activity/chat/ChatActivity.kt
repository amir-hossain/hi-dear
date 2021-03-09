package com.hi.dear.ui.activity.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.hi.dear.data.model.common.Chat
import com.hi.dear.databinding.ActivityChatBinding
import com.hi.dear.ui.base.BaseActivity


class ChatActivity : BaseActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: MsgAdapter
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarLayout.toolbarTitle.text = "Match"
        binding.toolbarLayout.back.setOnClickListener {
            onBackPressed()
        }

        adapter = MsgAdapter()
        binding.recyclerView.layoutAnimation = null
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = adapter
        binding.messageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.sendButton.isActivated = s?.toString()?.isNotBlank() == true
            }
        })

        binding.sendButton.setOnClickListener {
            var chat = Chat();
            ++count
            if (count % 2 != 0) {
                chat.isOwner = true
            }
            chat.text = binding.messageEditText.text.toString()
            adapter.addData(chat)
            binding.messageEditText.setText("")
            binding.recyclerView.smoothScrollToPosition(adapter.itemCount)
        }
    }
}