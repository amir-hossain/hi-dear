package com.hi.dear.ui.activity.match

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.ActivityProfileBinding
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.base.BaseActivity


class ProfileActivity : BaseActivity<ActivityProfileBinding, ViewModel>() {

    override fun initViewBinding(): ActivityProfileBinding {
        return ActivityProfileBinding.inflate(layoutInflater)
    }


    override fun initView() {
        val userData = intent.getParcelableExtra<UserCore>(ChatActivity.Args)!!
        binding.toolbarLayout.toolbarTitle.text = userData.name
        binding.toolbarLayout.back.setOnClickListener {
            super.onBackPressed()
        }
    }

    override fun attachObserver(viewModel: ViewModel?) {

    }

    override fun initViewModel(): ViewModel? {
        return null
    }

    override fun initLoadingView(isLoading: Boolean) {

    }

    companion object {
        const val Args = "args"
        fun start(context: Context, userData: UserCore?) {
            if (userData == null) {
                return
            }
            var intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(Args, userData)
            context.startActivity(intent)
        }
    }
}