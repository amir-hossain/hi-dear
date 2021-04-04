package com.hi.dear.ui.fragment.notification

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.FragmentNotificationBinding
import com.hi.dear.repo.NotificationRepository
import com.hi.dear.source.remote.FirebaseNotificationSource
import com.hi.dear.ui.Constant
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.base.BaseFragment


class NotificationFragment : BaseFragment<FragmentNotificationBinding, NotificationViewModel>(),
    NotificationAdapter.IMessageClickListener {

    private lateinit var adapter: NotificationAdapter

    override fun initViewBinding(inflater: LayoutInflater): FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(inflater)
    }

    override fun initViewModel(): NotificationViewModel? {
        return ViewModelProvider(
            this,
            ViewModelFactory(NotificationRepository(FirebaseNotificationSource()))
        )
            .get(NotificationViewModel::class.java)
    }

    override fun initView() {
        initAdapter()
        viewModel?.getNotifications()
    }

    private fun initAdapter() {
        adapter = NotificationAdapter(this)
        binding.notificationRv.adapter = adapter
        binding.notificationRv.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun attachObserver(viewModel: NotificationViewModel?) {
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

    override fun initLoadingView(isLoading: Boolean) {
    }

    override fun onItemClick(data: NotificationData) {
        if (data is UserCore && data.notificationType == Constant.notification_type_request_accepted) {
            ChatActivity.start(requireContext(), data)
        }
    }
}