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
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.main.MainActivity
import com.hi.dear.ui.base.BaseFragment
import com.hi.dear.ui.Constant as Constant1


class GiftFragment : BaseFragment<FragmentGiftBinding, GiftViewModel>() {

    private lateinit var giftDialogBinding: DialogGiftBinding
    private lateinit var dialog: AlertDialog

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
            viewModel?.giftCoin(
                Constant1.GiftCoint,
                PrefsManager.getInstance().readString(PrefsManager.UserId)!!
            )
            binding.contentGroup.visibility = View.GONE
            binding.emptyText.visibility = View.GONE
        }
    }

    private fun showDialog() {
        giftDialogBinding = DialogGiftBinding.inflate(layoutInflater)
       dialog = AlertDialog.Builder(requireContext())
            .setView(giftDialogBinding.root)
            .setCancelable(false)
            .create()
        giftDialogBinding.msg.text = getString(R.string.gift_msg, 10)
        giftDialogBinding.okBtn.setOnClickListener {
            Utils.disableView(giftDialogBinding.okBtn)
            viewModel?.setLastOpenDate(
                PrefsManager.getInstance().readString(PrefsManager.UserId)!!
            )
        }
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
                (requireActivity() as MainActivity).setRemainingCoin(Constant1.CurrentCoin+Constant1.GiftCoint)
                dialog.dismiss()
                binding.contentGroup.visibility = View.GONE
                binding.emptyText.visibility = View.VISIBLE
            } else {
                Utils.enableView(giftDialogBinding.okBtn)
            }
        })

        viewModel?.giftCoinResult?.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            if (result.success) {
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