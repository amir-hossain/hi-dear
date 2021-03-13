package com.hi.dear.ui.activity.chat

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModel
import com.hi.dear.data.model.common.Chat
import com.hi.dear.databinding.ActivityChatBinding
import com.hi.dear.ui.base.BaseActivity


class ChatActivity : BaseActivity<ActivityChatBinding, ViewModel>() {

    private lateinit var adapter: MsgAdapter
    private var count = 0

    override fun initViewBinding(): ActivityChatBinding {
        return ActivityChatBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initView() {
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

    override fun attachObserver(viewModel: ViewModel?) {

    }

    override fun initLoadingView(isLoading: Boolean) {

    }
}