package com.hi.dear.ui.activity.match

import androidx.lifecycle.ViewModel
import com.hi.dear.databinding.ActivityMatchBinding
import com.hi.dear.ui.base.BaseActivity


class MatchActivity : BaseActivity<ActivityMatchBinding, ViewModel>() {

    override fun initViewBinding(): ActivityMatchBinding {
        return ActivityMatchBinding.inflate(layoutInflater)
    }


    override fun initView() {
        binding.toolbarLayout.toolbarTitle.text = "Match"
    }

    override fun attachObserver(viewModel: ViewModel?) {

    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initLoadingView(isLoading: Boolean) {

    }
}