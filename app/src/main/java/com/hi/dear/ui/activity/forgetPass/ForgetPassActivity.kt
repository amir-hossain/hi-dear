package com.hi.dear.ui.activity.forgetPass

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.databinding.ActivityForgotBinding
import com.hi.dear.repo.ForgetPasswordRepository
import com.hi.dear.source.remote.FirebaseForgetPassSource
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.base.BaseActivity


class ForgetPassActivity : BaseActivity<ActivityForgotBinding, ForgotViewModel>() {

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    override fun initViewBinding(): ActivityForgotBinding {
        return ActivityForgotBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): ForgotViewModel {
        return ViewModelProvider(
            this,
            ViewModelFactory(ForgetPasswordRepository(FirebaseForgetPassSource()))
        )
            .get(ForgotViewModel::class.java)
    }

    override fun initView() {
        binding.back.setOnClickListener { onBackPressed() }

        binding.mobileOrEmail.apply {
            afterTextChanged {
                viewModel?.forgetPassDataChanged(
                    binding.mobileOrEmail.text.toString()
                )
            }

            binding.sendBtn.setOnClickListener {
                viewModel?.send(binding.mobileOrEmail.text.toString())
            }
        }
    }

    override fun attachObserver(viewModel: ForgotViewModel?) {
        viewModel?.forgetPassFormState?.observe(this@ForgetPassActivity, Observer {
            val loginState = it ?: return@Observer

            binding.sendBtn.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                binding.mobileOrEmail.error = getString(loginState.emailError)
            }
        })

        viewModel?.forgetPassResult?.observe(this@ForgetPassActivity, Observer {
            val requestResult = it ?: return@Observer
            if (requestResult.success) {
                PasswordRevelActivity.start(this, it.data!!)
                finish()
            } else {
                showToast(getString(requestResult.msg))
            }
        })
    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
        }
    }
}