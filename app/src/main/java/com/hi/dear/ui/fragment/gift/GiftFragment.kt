package com.hi.dear.ui.fragment.gift

import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import com.hi.dear.databinding.FragmentBoostProfileBinding
import com.hi.dear.databinding.FragmentGiftBinding
import com.hi.dear.databinding.FragmentTipsBinding
import com.hi.dear.ui.base.BaseFragment


class GiftFragment : BaseFragment<FragmentGiftBinding, ViewModel>() {

    override fun initViewBinding(inflater: LayoutInflater): FragmentGiftBinding {
        return FragmentGiftBinding.inflate(inflater)
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