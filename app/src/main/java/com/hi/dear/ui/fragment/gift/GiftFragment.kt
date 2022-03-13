package com.hi.dear.ui.fragment.gift

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.databinding.FragmentGiftBinding
import com.hi.dear.repo.GiftRepository
import com.hi.dear.source.remote.FirebaseGiftSource
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.base.BaseFragment


class GiftFragment : BaseFragment<FragmentGiftBinding, GiftViewModel>() {

    override fun initViewBinding(inflater: LayoutInflater): FragmentGiftBinding {
        return FragmentGiftBinding.inflate(inflater)
    }

    override fun initViewModel(): GiftViewModel? {
        return ViewModelProvider(
            this,
            ViewModelFactory(GiftRepository(FirebaseGiftSource()))
        )
            .get(GiftViewModel::class.java)
    }

    override fun initView() {
        viewModel?.isGiftAvailable(PrefsManager.getInstance().readString(PrefsManager.UserId)!!)
        binding.giftImg.setOnClickListener {
            viewModel?.setLastOpenDate(
                PrefsManager.getInstance().readString(PrefsManager.UserId)!!
            )
        }
    }

    override fun attachObserver(viewModel: GiftViewModel?) {
        viewModel?.giftAvailableResult?.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            if (result.success && result.data!!) {
                binding.contentGroup.visibility = View.VISIBLE
                binding.emptyText.visibility = View.GONE
            } else if (result.success && !result.data!!) {
                binding.contentGroup.visibility = View.GONE
                binding.emptyText.visibility = View.VISIBLE
            } else {
                binding.contentGroup.visibility = View.GONE
                binding.emptyText.visibility = View.VISIBLE
                showToast(it.msg)
            }
        })
        viewModel?.setLastTimeResult?.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                showToast("you get 10 coins")
            }
        })


    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}