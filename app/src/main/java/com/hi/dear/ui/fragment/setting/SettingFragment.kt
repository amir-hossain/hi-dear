package com.hi.dear.ui.fragment.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import com.hi.dear.BuildConfig
import com.hi.dear.R
import com.hi.dear.databinding.FragmentSettingBinding
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.base.BaseFragment


class SettingFragment : BaseFragment<FragmentSettingBinding, ViewModel>() {

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

    override fun initViewBinding(inflater: LayoutInflater): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(inflater)
    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initView() {
        emailDefaultSubject =
            "Feedback about ${getString(R.string.app_name)} (${BuildConfig.VERSION_NAME})"

        binding.btnLogout.setOnClickListener {
            DialogFactory.makeDialog(R.string.logout_msg, logoutBtnListener)
                .showDialog(activity?.supportFragmentManager)
        }
        binding.btnDelete.setOnClickListener {
            DialogFactory.makeDialog(R.string.account_delete_message, logoutBtnListener)
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
    }

    override fun attachObserver(viewModel: ViewModel?) {

    }
}