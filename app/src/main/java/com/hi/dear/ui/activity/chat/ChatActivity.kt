package com.hi.dear.ui.activity.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.ActivityChatBinding
import com.hi.dear.repo.ChatRepository
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.base.BaseActivity


class ChatActivity : BaseActivity<ActivityChatBinding, ChatViewModel>() {

    private lateinit var adapter: ChatAdapter
    private var count = 0

    override fun initViewBinding(): ActivityChatBinding {
        return ActivityChatBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): ChatViewModel? {
        return ViewModelProvider(
            this, ViewModelFactory(ChatRepository())
        ).get(ChatViewModel::class.java)
    }

    override fun initView() {
        val otherUserData = intent.getParcelableExtra<UserCore>(Args)!!
        binding.toolbarLayout.toolbarTitle.text = otherUserData.name
        binding.toolbarLayout.back.setOnClickListener {
            onBackPressed()
        }

        adapter = ChatAdapter()
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
            val text = binding.messageEditText.text.toString()
            viewModel?.sendMessage(text, otherUserData.id!!)
            binding.messageEditText.setText("")
        }
        viewModel?.getMessage(otherUserData.id!!)
    }

    override fun attachObserver(viewModel: ChatViewModel?) {
        viewModel?.chatSentResult?.observe(this@ChatActivity, Observer {
            val loginResult = it ?: return@Observer
            if (!loginResult.success) {
                showToast(getString(loginResult.msg))
            }

            setResult(RESULT_OK)
        })


        viewModel?.incomingChatData?.observe(this@ChatActivity, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.success) {
                adapter.submitList(it.data)
                adapter.notifyDataSetChanged()
                binding.recyclerView.smoothScrollToPosition(adapter.itemCount)
            } else {
                showToast(getString(loginResult.msg))
            }

            setResult(Activity.RESULT_OK)
        })
    }

    override fun initLoadingView(isLoading: Boolean) {

    }

    companion object {
        const val Args = "args"
        fun start(context: Context, userData: UserCore?) {
            if (userData == null) {
                return
            }
            var intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(Args, userData)
            context.startActivity(intent)
        }
    }
}