package com.hi.dear.ui.fragment.tips

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModel
import com.hi.dear.databinding.FragmentTipsBinding
import com.hi.dear.ui.base.BaseFragment


class TipsFragment : BaseFragment<FragmentTipsBinding, ViewModel>() {

    override fun initViewBinding(inflater: LayoutInflater): FragmentTipsBinding {
        return FragmentTipsBinding.inflate(inflater)
    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initView() {
        binding.splashGroup.visibility = View.VISIBLE
        binding.contentGroup.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.splashGroup.visibility = View.GONE
            binding.contentGroup.visibility = View.VISIBLE
        }, 2000)
    }

    override fun attachObserver(viewModel: ViewModel?) {

    }

    override fun initLoadingView(isLoading: Boolean) {

    }

}