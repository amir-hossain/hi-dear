package com.hi.dear.ui.fragment.gift

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.R
import com.hi.dear.databinding.DialogGiftBinding
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
            binding.contentGroup.visibility = View.GONE
            binding.emptyText.visibility = View.GONE
            viewModel?.setLastOpenDate(
                PrefsManager.getInstance().readString(PrefsManager.UserId)!!
            )
        }
    }

    private fun showDialog() {
        val giftDialogBinding = DialogGiftBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(giftDialogBinding.root)
            .setCancelable(false)
            .create()
        giftDialogBinding.msg.text = getString(R.string.gift_msg, 10)
        giftDialogBinding.okBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
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
                binding.contentGroup.visibility = View.GONE
                binding.emptyText.visibility = View.VISIBLE
                showDialog()
            } else {
                binding.contentGroup.visibility = View.VISIBLE
                binding.emptyText.visibility = View.GONE
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