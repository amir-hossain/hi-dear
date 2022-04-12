package com.hi.dear.ui.fragment.top

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hi.dear.databinding.FragmentTopProfileBinding
import com.hi.dear.repo.TopProfileRepository
import com.hi.dear.source.remote.FirebaseTopProfileSource
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.activity.profile.ProfileActivity
import com.hi.dear.ui.base.BaseFragment

class TopProfileFragment : BaseFragment<FragmentTopProfileBinding, TopProfileViewModel>(),
    TopProfileAdapter.ITopProfileClickListener {
    private lateinit var adapter: TopProfileAdapter

    private fun initAdapter() {
        adapter = TopProfileAdapter(this)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onCloseClick(data: TopProfileData) {

    }

    override fun onChatClick(data: TopProfileData) {
        ChatActivity.start(requireContext(), data)
    }

    override fun onImageClick(data: TopProfileData) {
        ProfileActivity.start(requireContext(), data,ProfileActivity.Mode.VIEW)
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentTopProfileBinding {
        return FragmentTopProfileBinding.inflate(inflater)
    }

    override fun initViewModel(): TopProfileViewModel {
        return ViewModelProvider(
            this,
            ViewModelFactory(TopProfileRepository(FirebaseTopProfileSource()))
        )
            .get(TopProfileViewModel::class.java)
    }

    override fun initView() {
        initAdapter()
        viewModel?.getTopProfile()
    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun attachObserver(viewModel: TopProfileViewModel?) {
        viewModel?.topProfileResult?.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                adjustView(result.data!!)
            } else {
                showToast(getString(result.msg))
            }
        })
    }

    private fun adjustView(data: MutableList<TopProfileData>) {
        if (data.isEmpty()) {
            binding.emptyText.visibility = View.VISIBLE
        } else {
            binding.rv.visibility = View.VISIBLE
            adapter.addData(data)
        }
    }

}