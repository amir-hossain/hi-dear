package com.hi.dear.ui.activity.profile

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.ActivityProfileBinding
import com.hi.dear.repo.ProfileRepository
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.base.BaseActivity


class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileViewModel>() {

    override fun initViewBinding(): ActivityProfileBinding {
        return ActivityProfileBinding.inflate(layoutInflater)
    }


    override fun initView() {
        val userData = intent.getParcelableExtra<UserCore>(ChatActivity.Args)!!
        binding.toolbarLayout.toolbarTitle.text = userData.name
        binding.toolbarLayout.back.setOnClickListener {
            super.onBackPressed()
        }
        viewModel?.getProfileData(userData.id!!)
    }

    override fun attachObserver(viewModel: ProfileViewModel?) {
        viewModel?.profileResult?.observe(this@ProfileActivity, Observer {
            val result = it ?: return@Observer

            if (result.success) {
                showData(result.data!!)
                showEditIcon()
            } else {
                showToast(result.msg)
            }
        })
    }

    private fun showEditIcon() {
        binding.btnNameEdit.visibility = View.VISIBLE
        binding.btnAgeEdit.visibility = View.VISIBLE
        binding.btnGenderEdit.visibility = View.VISIBLE
        binding.btnCountryEdit.visibility = View.VISIBLE
        binding.btnCityEdit.visibility = View.VISIBLE
        binding.btnAboutMeEdit.visibility = View.VISIBLE
    }

    private fun showData(data: ProfileData) {
        Glide.with(this)
            .load(data.picture)
            .into(binding.img1)

        binding.name.text = data.name
        binding.age.text = data.age
        binding.gender.text = data.gender
        binding.country.text = data.country
        binding.city.text = data.city
        binding.aboutMe.text = data.about
    }

    override fun initViewModel(): ProfileViewModel? {
        return ViewModelProvider(
            this, ViewModelFactory(ProfileRepository())
        ).get(ProfileViewModel::class.java)
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