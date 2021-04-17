package com.hi.dear.ui.activity.forgetPass

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.hi.dear.databinding.ActivityPasswordRevelBinding
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.base.BaseActivity

class PasswordRevelActivity : BaseActivity<ActivityPasswordRevelBinding, ViewModel>() {
    override fun initViewBinding(): ActivityPasswordRevelBinding {
        return ActivityPasswordRevelBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initView() {
        val pass = intent.getStringExtra(PasswordRevelActivity.Args)!!
        binding.passTxt.text = pass
        binding.toolbarLayout.toolbarTitle.text = "Password revel activity"
        binding.toolbarLayout.back.setOnClickListener {
            onBackPressed()
        }
        binding.loginBtn.setOnClickListener {
            LoginActivity.open(this)
            finish()
        }
    }

    override fun attachObserver(viewModel: ViewModel?) {

    }

    override fun initLoadingView(isLoading: Boolean) {

    }

    companion object {
        const val Args = "args"
        fun start(context: Context, password: String) {
            val intent = Intent(context, PasswordRevelActivity::class.java)
            intent.putExtra(Args, password)
            context.startActivity(intent)
        }
    }
}