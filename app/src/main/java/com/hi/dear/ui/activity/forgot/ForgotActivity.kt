package com.hi.dear.ui.activity.forgot

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.databinding.ActivityForgotBinding
import com.hi.dear.repo.ForgetPasswordRepository
import com.hi.dear.source.local.ForgetPassDataSource
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.base.BaseActivity


class ForgotActivity : BaseActivity<ActivityForgotBinding, ForgotViewModel>() {

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
            ViewModelFactory(ForgetPasswordRepository(ForgetPassDataSource(application)))
        )
            .get(ForgotViewModel::class.java)
    }

    override fun initView() {
        binding.back.setOnClickListener { onBackPressed() }

        binding.email.apply {
            afterTextChanged {
                viewModel?.forgetPassDataChanged(
                    binding.email.text.toString()
                )
            }

            binding.sendBtn.setOnClickListener {
                binding.loading.visibility = View.VISIBLE
                viewModel?.send(binding.email.text.toString())
            }
        }
    }

    override fun attachObserver(viewModel: ForgotViewModel?) {
        viewModel?.forgetPassFormState?.observe(this@ForgotActivity, Observer {
            val loginState = it ?: return@Observer

            binding.sendBtn.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                binding.email.error = getString(loginState.emailError)
            }
        })

        viewModel?.forgetPassResult?.observe(this@ForgotActivity, Observer {
            val requestResult = it ?: return@Observer

            binding.loading.visibility = View.GONE
            if (requestResult.success) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            } else {
                showToast(getString(requestResult.msg))
            }

            setResult(Activity.RESULT_OK)
        })
    }

    override fun initLoadingView(isLoading: Boolean) {

    }

}