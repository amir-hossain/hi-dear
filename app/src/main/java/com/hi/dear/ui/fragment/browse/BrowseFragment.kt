package com.hi.dear.ui.fragment.browse

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hi.dear.databinding.FragmentBrowseBinding
import com.hi.dear.ui.activity.match.MatchActivity
import com.hi.dear.ui.base.BaseFragment
import link.fls.swipestack.SwipeStack


class BrowseFragment : BaseFragment(), SwipeStack.SwipeStackListener,
    View.OnClickListener {

    private lateinit var binding: FragmentBrowseBinding
    private lateinit var mSwipeStack: SwipeStack
    private lateinit var mAdapter: SwipeStackAdapter
    private lateinit var mData: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBrowseBinding.inflate(inflater)
        mSwipeStack = binding.swipeStack
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mData = ArrayList()
        mAdapter = SwipeStackAdapter(mData, requireContext())
        mSwipeStack.setListener(this)
        binding.swipeStack.adapter = mAdapter
        binding.heartBtn.setOnClickListener {
            startActivity(Intent(requireContext(), MatchActivity::class.java))
        }
        fillWithTestData()
    }

    private fun fillWithTestData() {
        for (x in 0..4) {
            mData.add("hello ${(x + 1)}")
        }
    }

    override fun onViewSwipedToLeft(position: Int) {

    }

    override fun onViewSwipedToRight(position: Int) {

    }

    override fun onStackEmpty() {
    }

    override fun onClick(v: View?) {

    }

}