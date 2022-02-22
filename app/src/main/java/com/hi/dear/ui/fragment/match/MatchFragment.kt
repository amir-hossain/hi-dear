package com.hi.dear.ui.fragment.match

import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.FragmentMatchBinding
import com.hi.dear.repo.RequestRepository
import com.hi.dear.source.remote.FirebaseRequestSource
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.activity.profile.ProfileActivity
import com.hi.dear.ui.base.BaseFragment


class MatchFragment : BaseFragment<FragmentMatchBinding, RequestViewModel>(),
    MatchAdapter.IRequestClickListener {
    private lateinit var adapter: MatchAdapter

    private fun initAdapter() {
        adapter = MatchAdapter(this)
        binding.messageRv.adapter = adapter
        binding.messageRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onCloseClick(data: RequestData) {

    }

    override fun onAcceptClick(data: RequestData) {
        viewModel?.reactToRequest(true, data)
    }

    override fun onNoClick(data: RequestData) {
        viewModel?.reactToRequest(false, data)
    }

    override fun onChatClick(data: RequestData) {
        ChatActivity.start(requireContext(), data)
    }

    override fun onImageClick(data: RequestData) {
        ProfileActivity.start(requireContext(), data)
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentMatchBinding {
        return FragmentMatchBinding.inflate(inflater)
    }

    override fun initViewModel(): RequestViewModel {
        return ViewModelProvider(
            this,
            ViewModelFactory(RequestRepository(FirebaseRequestSource()))
        )
            .get(RequestViewModel::class.java)
    }

    override fun initView() {
        initAdapter()
        viewModel?.getRequest()
    }

    override fun attachObserver(viewModel: RequestViewModel?) {
        viewModel?.requestResult?.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                adapter.addData(result.data!!)
            } else {
                showToast(getString(result.msg))
            }
        })

        viewModel?.requestReactResult?.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                adapter.updateView(result.data!!)
            }
            showToast(result.msg)
        })
    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            adapter.disableButton()
        } else {
            adapter.enableButton()
        }
    }
}