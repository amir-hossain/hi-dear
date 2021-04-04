package com.hi.dear.ui.activity.message

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.ActivityMessageBinding
import com.hi.dear.repo.MessageRepository
import com.hi.dear.source.remote.FirebaseMsgSource
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.base.BaseActivity


class MessageActivity : BaseActivity<ActivityMessageBinding, MessageViewModel>(),
    MessageAdapter.IMessageClickListener {

    private lateinit var adapter: MessageAdapter

    override fun initViewModel(): MessageViewModel? {
        return ViewModelProvider(
            this,
            ViewModelFactory(MessageRepository(FirebaseMsgSource()))
        )
            .get(MessageViewModel::class.java)
    }

    override fun initView() {
        binding.toolbarLayout.toolbarTitle.text = "Messages"
        binding.toolbarLayout.back.setOnClickListener {
            onBackPressed()
        }
        initAdapter()
        viewModel?.getMessage()
    }

    private fun initAdapter() {
        adapter = MessageAdapter(this)
        binding.messageRv.adapter = adapter
        binding.messageRv.layoutManager = LinearLayoutManager(this)
    }

    override fun attachObserver(viewModel: MessageViewModel?) {
        viewModel?.liveResult?.observe(this, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                adapter.submitList(result.data!!)
                adapter.notifyDataSetChanged()
            } else {
                showToast(getString(result.msg))
            }
        })
    }

    override fun initLoadingView(isLoading: Boolean) {
    }

    override fun onItemClick(data: MessageData) {
        if (data is UserCore) {
            ChatActivity.start(this, data)
        }

    }

    override fun initViewBinding(): ActivityMessageBinding {
        return ActivityMessageBinding.inflate(layoutInflater)
    }
}