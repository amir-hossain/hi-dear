package com.hi.dear.ui.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hi.dear.R
import com.hi.dear.databinding.FragmentSettingBinding
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.base.BaseFragment


class SettingFragment : BaseFragment(), DialogFactory.ITwoBtnListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSettingBinding.inflate(inflater)
        DialogFactory.makeDialog(R.string.welcome, this)
            .showDialog(activity?.supportFragmentManager)

        return binding.root
    }

    override fun onPositiveBtnClicked() {

    }

    override fun onNegativeBtnClicked() {

    }

}