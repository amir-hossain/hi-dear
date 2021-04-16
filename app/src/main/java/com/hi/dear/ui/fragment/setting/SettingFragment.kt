package com.hi.dear.ui.fragment.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.BuildConfig
import com.hi.dear.R
import com.hi.dear.databinding.FragmentSettingBinding
import com.hi.dear.repo.SettingRepository
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.base.BaseFragment


class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>() {

    private lateinit var emailDefaultSubject: String
    private val emailAddress = arrayOf("hidearapp@gmail.com")

    private val logoutBtnListener = object : DialogFactory.ITwoBtnListener {
        override fun onPositiveBtnClicked() {
            PrefsManager.getInstance().clearData()
            LoginActivity.open(requireContext())
        }

        override fun onNegativeBtnClicked() {

        }
    }

    private val deleteBtnListener = object : DialogFactory.ITwoBtnListener {
        override fun onPositiveBtnClicked() {
            viewModel?.deleteAccount()
        }

        override fun onNegativeBtnClicked() {

        }
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater)
    }

    override fun initViewModel(): SettingViewModel? {
        return ViewModelProvider(
            this, ViewModelFactory(SettingRepository())
        ).get(SettingViewModel::class.java)
    }

    override fun initView() {
        emailDefaultSubject =
            "Feedback about ${getString(R.string.app_name)} (${BuildConfig.VERSION_NAME})"

        binding.btnLogout.setOnClickListener {
            DialogFactory.makeDialog(R.string.logout_msg, logoutBtnListener)
                .showDialog(activity?.supportFragmentManager)
        }
        binding.btnDelete.setOnClickListener {
            DialogFactory.makeDialog(R.string.account_delete_message, deleteBtnListener)
                .showDialog(activity?.supportFragmentManager)
        }

        binding.btnContactUs.setOnClickListener {
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress)
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, emailDefaultSubject)
            open(mailIntent)
        }
    }

    private fun open(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showToast(R.string.email_client_not_found)
        }
    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    override fun attachObserver(viewModel: SettingViewModel?) {
        viewModel?.deleteResult?.observe(this@SettingFragment, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                PrefsManager.getInstance().clearData()
                LoginActivity.open(requireContext())
            } else {
                showToast(getString(result.msg))
            }
        })
    }
}