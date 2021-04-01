package com.hi.dear.ui.fragment.message

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.FragmentMessageBinding
import com.hi.dear.repo.MessageRepository
import com.hi.dear.source.remote.FirebaseMsgSource
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.base.BaseFragment


class MessageFragment : BaseFragment<FragmentMessageBinding, MessageViewModel>(),
    MessageAdapter.IMessageClickListener {

    private lateinit var adapter: MessageAdapter

    override fun initViewBinding(inflater: LayoutInflater): FragmentMessageBinding {
        return FragmentMessageBinding.inflate(inflater)
    }

    override fun initViewModel(): MessageViewModel? {
        return ViewModelProvider(
            this,
            ViewModelFactory(MessageRepository(FirebaseMsgSource()))
        )
            .get(MessageViewModel::class.java)
    }

    override fun initView() {
        initAdapter()
        viewModel?.getMessage()
        viewModel?.liveResult?.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                adapter.submitList(result.data!!)
                adapter.notifyDataSetChanged()
            } else {
                showToast(getString(result.msg))
            }
        })
    }

    private fun initAdapter() {
        adapter = MessageAdapter(this)
        binding.messageRv.adapter = adapter
        binding.messageRv.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun attachObserver(viewModel: MessageViewModel?) {

    }

    override fun initLoadingView(isLoading: Boolean) {
    }

    override fun onItemClick(data: MessageData) {
        if (data is UserCore) {
            ChatActivity.start(requireContext(), data)
        }

    }

}