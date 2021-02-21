package com.hi.dear.ui.fragment.tips

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hi.dear.databinding.FragmentTipsBinding
import com.hi.dear.ui.base.BaseFragment


class TipsFragment : BaseFragment() {

    private lateinit var binding: FragmentTipsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTipsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.splashGroup.visibility = View.VISIBLE
        binding.contentGroup.visibility = View.GONE
        Handler(Looper.getMainLooper()).postDelayed({
            binding.splashGroup.visibility = View.GONE
            binding.contentGroup.visibility = View.VISIBLE
        }, 2000)
    }

}