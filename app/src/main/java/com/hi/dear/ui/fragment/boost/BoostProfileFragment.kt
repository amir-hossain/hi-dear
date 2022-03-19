package com.hi.dear.ui.fragment.boost

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.R
import com.hi.dear.databinding.FragmentBoostProfileBinding
import com.hi.dear.repo.BoostProfileRepository
import com.hi.dear.source.remote.FirebaseBoostProfileSource
import com.hi.dear.ui.Constant
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.Utils.disableView
import com.hi.dear.ui.Utils.enableView
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.base.BaseFragment
import com.hi.dear.ui.fragment.top.TopProfileData
import java.text.SimpleDateFormat
import java.util.*


class BoostProfileFragment : BaseFragment<FragmentBoostProfileBinding, BoostProfileViewModel>() {
    private val smallBoostBtnListener = object : DialogFactory.ITwoBtnListener {
        override fun onPositiveBtnClicked() {
            viewModel?.boostProfile(generateTopProfileData(Constant.SmallBoostTime))
        }

        override fun onNegativeBtnClicked() {

        }
    }

    private val largeBoostBtnListener = object : DialogFactory.ITwoBtnListener {
        override fun onPositiveBtnClicked() {
            viewModel?.boostProfile(generateTopProfileData(Constant.LargeBoostTime))

        }

        override fun onNegativeBtnClicked() {

        }
    }

    private fun generateTopProfileData(boostTime: Int): TopProfileData {
        val manager = PrefsManager.getInstance()
        val data = TopProfileData()
        data.id = manager.readString(PrefsManager.UserId)
        data.name = manager.readString(PrefsManager.userName)
        data.picture = manager.readString(PrefsManager.Pic)
        data.endTime = System.currentTimeMillis() + getBoostTimeInMillis(boostTime)
        data.emailOrMobile = manager.readString(PrefsManager.emailOrMobile)
        data.gender = manager.readString(PrefsManager.Gender)
        return data
    }

    private fun getBoostTimeInMillis(boostTimeInMinit: Int): Long {
        return (boostTimeInMinit * 60 * 1000).toLong()
    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentBoostProfileBinding {
        return FragmentBoostProfileBinding.inflate(inflater)
    }

    override fun initViewModel(): BoostProfileViewModel? {
        return ViewModelProvider(
            this,
            ViewModelFactory(BoostProfileRepository(FirebaseBoostProfileSource()))
        )
            .get(BoostProfileViewModel::class.java)
    }

    override fun initView() {
        binding.smallBoost.setOnClickListener {
            DialogFactory.makeDialog(R.string.small_boost_confirm_msg, smallBoostBtnListener)
                .showDialog(activity?.supportFragmentManager)
            disableView(binding.largeBoost)
            disableView(binding.smallBoost)
        }

        binding.largeBoost.setOnClickListener {
            DialogFactory.makeDialog(R.string.large_boost_confirm_msg, largeBoostBtnListener)
                .showDialog(activity?.supportFragmentManager)
            disableView(binding.largeBoost)
            disableView(binding.smallBoost)
        }
    }

    override fun attachObserver(viewModel: BoostProfileViewModel?) {
        viewModel?.boostProfileResult?.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                disableView(binding.largeBoost)
                disableView(binding.smallBoost)
                binding.msg.text = getString(R.string.boost_time_msg,getRemainingTime(result.data))
            } else {
                enableView(binding.largeBoost)
                enableView(binding.smallBoost)
            }
        })
    }

    private fun getRemainingTime(time: Long?): String {
        return SimpleDateFormat("h:mm a").format(Date(time!!))
    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}