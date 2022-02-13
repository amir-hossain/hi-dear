package com.hi.dear.ui.fragment.boost

import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import com.hi.dear.databinding.FragmentBoostProfileBinding
import com.hi.dear.databinding.FragmentTipsBinding
import com.hi.dear.ui.base.BaseFragment


class BoostProfileFragment : BaseFragment<FragmentBoostProfileBinding, ViewModel>() {

    override fun initViewBinding(inflater: LayoutInflater): FragmentBoostProfileBinding {
        return FragmentBoostProfileBinding.inflate(inflater)
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