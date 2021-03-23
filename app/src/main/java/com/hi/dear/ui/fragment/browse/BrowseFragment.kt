package com.hi.dear.ui.fragment.browse

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.FragmentBrowseBinding
import com.hi.dear.repo.BrowseRepository
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.match.MatchActivity
import com.hi.dear.ui.base.BaseFragment
import link.fls.swipestack.SwipeStack


class BrowseFragment : BaseFragment<FragmentBrowseBinding, BrowseViewModel>(),
    SwipeStack.SwipeStackListener,
    View.OnClickListener {

    private lateinit var mSwipeStack: SwipeStack
    private lateinit var mAdapter: SwipeStackAdapter
    private lateinit var mData: MutableList<UserCore>

    override fun onViewSwipedToLeft(position: Int) {

    }

    override fun onViewSwipedToRight(position: Int) {

    }

    override fun onStackEmpty() {
    }

    override fun onClick(v: View?) {

    }

    override fun initViewBinding(inflater: LayoutInflater): FragmentBrowseBinding {
        return FragmentBrowseBinding.inflate(inflater)
    }

    override fun initViewModel(): BrowseViewModel? {
        return ViewModelProvider(
            this, ViewModelFactory(BrowseRepository())
        ).get(BrowseViewModel::class.java)
    }

    override fun initView() {
        viewModel?.getBrowseData("male", 5)
        mSwipeStack = binding.swipeStack
        mData = ArrayList()
        mAdapter = SwipeStackAdapter(mData, requireContext())
        mSwipeStack.setListener(this)
        binding.swipeStack.adapter = mAdapter
        binding.heartBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MatchActivity::class.java))
        }
    }

    override fun attachObserver(viewModel: BrowseViewModel?) {
        viewModel?.result?.observe(this@BrowseFragment, Observer {
            val browseResult = it ?: return@Observer
            if (browseResult.success) {
                fillWithTestData()
                mAdapter.notifyDataSetChanged()
            } else {
                showToast(getString(browseResult.msg))
            }

            requireActivity().setResult(Activity.RESULT_OK)
        })
    }

    override fun initLoadingView(isLoading: Boolean) {
    }

    private fun fillWithTestData() {
        for (x in 0..4) {
            mData.add(UserCore())
        }
    }

}