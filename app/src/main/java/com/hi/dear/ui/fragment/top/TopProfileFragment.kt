package com.hi.dear.ui.fragment.top

import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import com.hi.dear.databinding.FragmentBoostProfileBinding
import com.hi.dear.databinding.FragmentTipsBinding
import com.hi.dear.databinding.FragmentTopProfileBinding
import com.hi.dear.ui.base.BaseFragment


class TopProfileFragment : BaseFragment<FragmentTopProfileBinding, ViewModel>() {

    override fun initViewBinding(inflater: LayoutInflater): FragmentTopProfileBinding {
        return FragmentTopProfileBinding.inflate(inflater)
    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initView() {

    }

    override fun attachObserver(viewModel: ViewModel?) {

    }

    override fun initLoadingView(isLoading: Boolean) {

    }

}