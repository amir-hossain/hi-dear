package com.hi.dear.ui.activity.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.ActivityLoginBinding
import com.hi.dear.repo.LoginRepository
import com.hi.dear.ui.PasswordTransformation
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.forgot.ForgotActivity
import com.hi.dear.ui.activity.main.MainActivity
import com.hi.dear.ui.activity.register.RegistrationActivity
import com.hi.dear.ui.base.BaseActivity


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    override fun initViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): LoginViewModel {
        return ViewModelProvider(
            this, ViewModelFactory(LoginRepository())
        ).get(LoginViewModel::class.java)
    }

    override fun initView() {
        Utils.disableView(binding.login)
        binding.password.transformationMethod = PasswordTransformation()

        binding.back.setOnClickListener { onBackPressed() }

        binding.forgetPass.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    ForgotActivity::class.java
                )
            )
        }

        binding.id.afterTextChanged {
            viewModel?.loginDataChanged(
                binding.id.text.toString(),
                binding.password.text.toString()
            )
        }

        binding.password.apply {
            afterTextChanged {
                viewModel?.loginDataChanged(
                    binding.id.text.toString(),
                    binding.password.text.toString()
                )
            }

            binding.login.setOnClickListener {
                viewModel?.login(binding.id.text.toString(), binding.password.text.toString())
            }

            binding.register.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
            }
        }
    }

    override fun attachObserver(viewModel: LoginViewModel?) {
        viewModel?.loginFormState?.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            if (loginState.isDataValid) {
                Utils.enableView(binding.login)
            } else {
                Utils.disableView(binding.login)
            }

            if (loginState.idError != null) {
                binding.id.error = getString(loginState.idError)
            }
            if (loginState.passwordError != null) {
                binding.password.error = getString(loginState.passwordError)
            }
        })

        viewModel?.loginResult?.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.success) {
                PrefsManager.getInstance().writeBoolean(PrefsManager.IS_LOGGED_IN, true)
                saveUserData(it.data)
                PrefsManager.getInstance().writeBoolean(PrefsManager.IS_LOGGED_IN, true)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                showToast(getString(loginResult.msg))
            }

            setResult(Activity.RESULT_OK)
        })
    }

    private fun saveUserData(userData: UserCore?) {
        if (userData == null) {
            return
        }

        PrefsManager.getInstance().writeString(PrefsManager.Gender, userData.gender!!)
        PrefsManager.getInstance().writeString(PrefsManager.UserId, userData.id!!)
        PrefsManager.getInstance().writeString(PrefsManager.userName, userData.name!!)
        PrefsManager.getInstance().writeString(PrefsManager.emailOrMobile, userData.emailOrMobile!!)
        PrefsManager.getInstance().writeString(PrefsManager.Pic, userData.picture!!)
    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            binding.loading.visibility = View.VISIBLE
            Utils.disableView(binding.login)
        } else {
            binding.loading.visibility = View.GONE
            Utils.enableView(binding.login)
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

