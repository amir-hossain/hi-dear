package com.hi.dear.ui.activity.forgot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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


class ForgotActivity : BaseActivity() {

    private lateinit var forgotViewModel: ForgotViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        forgotViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ForgetPasswordRepository(ForgetPassDataSource(application)))
        )
            .get(ForgotViewModel::class.java)

        binding.back.setOnClickListener { onBackPressed() }

        forgotViewModel.forgetPassFormState.observe(this@ForgotActivity, Observer {
            val loginState = it ?: return@Observer

            binding.sendBtn.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                binding.email.error = getString(loginState.emailError)
            }
        })

        forgotViewModel.forgetPassResult.observe(this@ForgotActivity, Observer {
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


        binding.email.apply {
            afterTextChanged {
                forgotViewModel.forgetPassDataChanged(
                    binding.email.text.toString()
                )
            }

            binding.sendBtn.setOnClickListener {
                binding.loading.visibility = View.VISIBLE
                forgotViewModel.send(binding.email.text.toString())
            }
        }
    }

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

}