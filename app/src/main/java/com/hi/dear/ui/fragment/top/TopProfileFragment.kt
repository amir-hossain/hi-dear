package com.hi.dear.ui.fragment.top

import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.FragmentTopProfileBinding
import com.hi.dear.repo.RequestRepository
import com.hi.dear.source.remote.FirebaseRequestSource
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.activity.profile.ProfileActivity
import com.hi.dear.ui.base.BaseFragment
import com.hi.dear.ui.fragment.match.MatchAdapter
import com.hi.dear.ui.fragment.match.RequestData
import com.hi.dear.ui.fragment.match.RequestViewModel

class TopProfileFragment : BaseFragment<FragmentTopProfileBinding, ViewModel>(),TopProfileAdapter.ITopProfileClickListener {
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
        ProfileActivity.start(requireContext(), data)
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentTopProfileBinding {
        return FragmentTopProfileBinding.inflate(inflater)
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
//        viewModel?.getRequest()
    }

    override fun initLoadingView(isLoading: Boolean) {

    }

    override fun attachObserver(viewModel: ViewModel?) {
        adapter.addData(getData())
    }

    private fun getData(): MutableList<TopProfileData> {
        val list = mutableListOf<TopProfileData>()
        val data=TopProfileData()
        data.id="1"
        data.name="1"
        data.picture="1"
        data.gender="male"
        data.emailOrMobile="0111111"
        list.add(data)
        return  list
    }
}