package com.hi.dear.ui.activity.login

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hi.dear.R
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.ActivityLoginBinding
import com.hi.dear.repo.LoginRepository
import com.hi.dear.source.local.LocalLoginSource
import com.hi.dear.ui.PasswordTransformation
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.activity.main.MainActivity
import com.hi.dear.ui.activity.register.RegistrationActivity
import com.hi.dear.ui.base.BaseActivity
import jp.wasabeef.glide.transformations.BlurTransformation


class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = binding.id
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        burBackground(binding.container, R.drawable.img_background)
        binding.password.transformationMethod = PasswordTransformation()
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(LoginRepository(LocalLoginSource(application)))
        )
            .get(LoginViewModel::class.java)

        binding.back.setOnClickListener { onBackPressed() }

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.idError != null) {
                id.error = getString(loginState.idError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.success) {
                PrefsManager.getInstance(this).writeBoolean(PrefsManager.IS_LOGGED_IN, true);
                updateUiWithUser(loginResult.data)
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                showToast(getString(loginResult.msg))
            }

            setResult(Activity.RESULT_OK)
        })

        id.afterTextChanged {
            loginViewModel.loginDataChanged(
                id.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    id.text.toString(),
                    password.text.toString()
                )
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(id.text.toString(), password.text.toString())
            }

            binding.register.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))
            }
        }
    }

    private fun burBackground(view: View, imageId: Int) {
        Glide.with(this)
            .load(imageId)
            .apply(bitmapTransform(BlurTransformation(100)))
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    view.background = resource
                }
            })
    }

    private fun updateUiWithUser(model: UserCore?) {
        val welcome = getString(R.string.welcome)
        val displayName = model?.name
        PrefsManager.getInstance(this).writeString(PrefsManager.UserId, model!!.id)
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}