package com.hi.dear.ui.fragment.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hi.dear.databinding.FragmentMessageBinding
import com.hi.dear.repo.MessageRepository
import com.hi.dear.source.local.LocalMessageSource
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.base.BaseFragment


class MessageFragment : BaseFragment(), MessageAdapter.IMessageClickListener {

    private lateinit var viewModel: MessageViewModel
    private lateinit var binding: FragmentMessageBinding
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(MessageRepository(LocalMessageSource(requireActivity().application)))
        )
            .get(MessageViewModel::class.java)
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
        viewModel.getMessage()
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
        adapter = MessageAdapter(this)
        binding.messageRv.adapter = adapter
        binding.messageRv.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onBlockClick(data: MessageData) {

    }

    override fun onBrowseClick(data: MessageData) {

    }

}