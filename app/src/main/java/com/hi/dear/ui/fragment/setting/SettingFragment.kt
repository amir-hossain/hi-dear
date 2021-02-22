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

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogout.setOnClickListener {
            DialogFactory.makeDialog(R.string.logout_msg, this)
                .showDialog(activity?.supportFragmentManager)
        }
        binding.btnDelete.setOnClickListener {
            DialogFactory.makeDialog(R.string.account_delete_message, this)
                .showDialog(activity?.supportFragmentManager)
        }
    }

    override fun onPositiveBtnClicked() {

    }

    override fun onNegativeBtnClicked() {

    }

}