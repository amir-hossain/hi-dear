package com.hi.dear.ui.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModel
import com.hi.dear.R
import com.hi.dear.databinding.FragmentSettingBinding
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.base.BaseFragment


class SettingFragment : BaseFragment<FragmentSettingBinding, ViewModel>(),
    DialogFactory.ITwoBtnListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    override fun onPositiveBtnClicked() {

    }

    override fun onNegativeBtnClicked() {

    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater)
    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initView() {
        binding.btnLogout.setOnClickListener {
            DialogFactory.makeDialog(R.string.logout_msg, this)
                .showDialog(activity?.supportFragmentManager)
        }
        binding.btnDelete.setOnClickListener {
            DialogFactory.makeDialog(R.string.account_delete_message, this)
                .showDialog(activity?.supportFragmentManager)
        }
    }

    override fun initLoadingView(isLoading: Boolean) {
        TODO("Not yet implemented")
    }

    override fun attachObserver(viewModel: ViewModel?) {
        TODO("Not yet implemented")
    }

}