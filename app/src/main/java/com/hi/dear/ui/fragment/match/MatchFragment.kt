package com.hi.dear.ui.fragment.match

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.hi.dear.databinding.FragmentMessageBinding
import com.hi.dear.repo.MatchRepository
import com.hi.dear.source.local.LocalMatchSource
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.base.BaseFragment


class MatchFragment : BaseFragment(), MatchAdapter.IMatchClickListener {

    private lateinit var viewModel: MatchViewModel
    private lateinit var binding: FragmentMessageBinding
    private lateinit var adapter: MatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(MatchRepository(LocalMatchSource(requireActivity().application)))
        )
            .get(MatchViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        viewModel.getMatch()
        viewModel.liveResult.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            if (result.success) {
                adapter.addData(result.data!!)
            } else {
                showToast(getString(result.msg))
            }
        })
    }

    private fun initAdapter() {
        adapter = MatchAdapter(this)
        binding.messageRv.adapter = adapter
        binding.messageRv.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onCloseClick(data: MatchData) {

    }

    override fun onAcceptClick(data: MatchData) {

    }

    override fun onNoClick(data: MatchData) {
    }
}