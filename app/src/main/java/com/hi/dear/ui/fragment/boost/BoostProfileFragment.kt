package com.hi.dear.ui.fragment.boost

import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import com.hi.dear.R
import com.hi.dear.databinding.FragmentBoostProfileBinding
import com.hi.dear.databinding.FragmentTipsBinding
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.base.BaseFragment


class BoostProfileFragment : BaseFragment<FragmentBoostProfileBinding, ViewModel>() {
    private val boost10BtnListener = object : DialogFactory.ITwoBtnListener {
        override fun onPositiveBtnClicked() {

        }

        override fun onNegativeBtnClicked() {

        }
    }

    private val boost30BtnListener = object : DialogFactory.ITwoBtnListener {
        override fun onPositiveBtnClicked() {

        }

        override fun onNegativeBtnClicked() {

        }
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentBoostProfileBinding {
        return FragmentBoostProfileBinding.inflate(inflater)
    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initView() {
        binding.boost10.setOnClickListener {
            DialogFactory.makeDialog(R.string.boost_10_confirm_msg, boost10BtnListener)
                .showDialog(activity?.supportFragmentManager)
        }

        binding.boost30.setOnClickListener {
            DialogFactory.makeDialog(R.string.boost_30_confirm_msg, boost30BtnListener)
                .showDialog(activity?.supportFragmentManager)
        }
    }

    override fun attachObserver(viewModel: ViewModel?) {

    }

    override fun initLoadingView(isLoading: Boolean) {

    }

}