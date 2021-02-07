package com.hi.dear.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.hi.dear.databinding.ActivitySplashBinding
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.activity.main.MainActivity
import com.hi.dear.ui.base.BaseActivity

class SplashScreenActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val isLoggedIn = PrefsManager.getInstance(this).readBoolean(PrefsManager.IS_LOGGED_IN)
        Handler(mainLooper).postDelayed({
            if (isLoggedIn) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            } else {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }, 3000)
    }
}