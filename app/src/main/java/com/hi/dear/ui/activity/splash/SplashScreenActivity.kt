package com.hi.dear.ui.activity.splash

import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.lifecycle.ViewModel
import com.hi.dear.databinding.ActivitySplashBinding
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.activity.main.MainActivity
import com.hi.dear.ui.activity.register.RegistrationActivity
import com.hi.dear.ui.base.BaseActivity

class SplashScreenActivity : BaseActivity<ActivitySplashBinding, ViewModel>() {

    override fun initViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initView() {
        val isLoggedIn = PrefsManager.getInstance(this).readBoolean(PrefsManager.IS_LOGGED_IN)
        if (isLoggedIn) {
            Handler(mainLooper).postDelayed({
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }, 2000)
        } else {
            binding.loginBtn.visibility = View.VISIBLE
            binding.signUpBtn.visibility = View.VISIBLE
        }
        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    override fun attachObserver(viewModel: ViewModel?) {

    }

    override fun initLoadingView(isLoading: Boolean) {

    }
}