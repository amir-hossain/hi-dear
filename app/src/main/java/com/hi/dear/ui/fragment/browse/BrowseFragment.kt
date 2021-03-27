package com.hi.dear.ui.fragment.browse

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.FragmentBrowseBinding
import com.hi.dear.repo.BrowseRepository
import com.hi.dear.ui.Constant
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.base.BaseFragment
import com.yuyakaido.android.cardstackview.*

class BrowseFragment : BaseFragment<FragmentBrowseBinding, BrowseViewModel>(), CardStackListener {

    private var visibleUserData: UserCore? = null
    private val mAdapter by lazy { SwipeStackAdapter() }
    private val manager by lazy { CardStackLayoutManager(requireContext(), this) }

    override fun initViewBinding(inflater: LayoutInflater): FragmentBrowseBinding {
        return FragmentBrowseBinding.inflate(inflater)
    }

    override fun initViewModel(): BrowseViewModel? {
        return ViewModelProvider(
            this, ViewModelFactory(BrowseRepository())
        ).get(BrowseViewModel::class.java)
    }

    override fun initView() {
        viewModel?.getBrowseData(getPreferredGender(), 5)
        manager.setStackFrom(StackFrom.BottomAndRight)
        manager.setSwipeableMethod(SwipeableMethod.Automatic)
        binding.swipeStack.adapter = mAdapter
        binding.swipeStack.layoutManager = manager


        binding.heartBtn.setOnClickListener {
            if (visibleUserData != null) {
                viewModel?.sendRequest(visibleUserData!!)
            }

        }
        binding.crossBtn.setOnClickListener {
            binding.swipeStack.swipe()
        }
    }

    private fun getPreferredGender(): String {
        var gender = PrefsManager.getInstance().readString(PrefsManager.Gender)
        return if (gender == null || gender == Constant.male) {
            Constant.female
        } else {
            Constant.male
        }
    }

    override fun attachObserver(viewModel: BrowseViewModel?) {
        viewModel?.browseDataResult?.observe(this@BrowseFragment, Observer {
            val browseResult = it ?: return@Observer
            if (browseResult.success) {
                mAdapter.addItem(it.data!!)
                mAdapter.notifyDataSetChanged()
            } else {
                showToast(browseResult.msg)
            }
            requireActivity().setResult(Activity.RESULT_OK)
        })

        viewModel?.requestDataResult?.observe(this@BrowseFragment, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                binding.swipeStack.swipe()
            }
            showToast(result.msg)
            requireActivity().setResult(Activity.RESULT_OK)
        })
    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            Utils.disableView(binding.heartBtn)
            Utils.disableView(binding.crossBtn)
        } else {
            Utils.enableView(binding.heartBtn)
            Utils.enableView(binding.crossBtn)
        }
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {

    }

    override fun onCardRewound() {

    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {
        visibleUserData = mAdapter.getItemBy(position)
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }
}