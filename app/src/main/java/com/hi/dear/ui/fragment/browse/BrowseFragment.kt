package com.hi.dear.ui.fragment.browse

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.databinding.FragmentBrowseBinding
import com.hi.dear.repo.BrowseRepository
import com.hi.dear.ui.Constant
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.match.MatchActivity
import com.hi.dear.ui.base.BaseFragment
import link.fls.swipestack.SwipeStack


class BrowseFragment : BaseFragment<FragmentBrowseBinding, BrowseViewModel>(),
    SwipeStack.SwipeStackListener,
    View.OnClickListener {

    private lateinit var mAdapter: SwipeStackAdapter

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
        viewModel?.getBrowseData(getPreferredGender(), 5)
        binding.swipeStack.setListener(this)
        mAdapter = SwipeStackAdapter(requireContext())
        binding.swipeStack.adapter = mAdapter
        binding.heartBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MatchActivity::class.java))
        }
    }

    private fun getPreferredGender(): String {
        var gender = PrefsManager.getInstance(requireContext()).readString(PrefsManager.gender)
        return if (gender == null || gender == Constant.male) {
            Constant.female
        } else {
            Constant.male
        }
    }

    override fun attachObserver(viewModel: BrowseViewModel?) {
        viewModel?.result?.observe(this@BrowseFragment, Observer {
            val browseResult = it ?: return@Observer
            if (browseResult.success) {
                mAdapter.addItem(it.data!!)
                mAdapter.notifyDataSetChanged()
            } else {
                showToast(getString(browseResult.msg))
            }
            requireActivity().setResult(Activity.RESULT_OK)
        })
    }

    override fun initLoadingView(isLoading: Boolean) {
    }
}