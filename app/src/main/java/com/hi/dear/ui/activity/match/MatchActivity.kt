package com.hi.dear.ui.activity.match

import android.os.Bundle
import android.view.View
import com.hi.dear.databinding.ActivityMatchBinding
import com.hi.dear.ui.base.BaseActivity


class MatchActivity : BaseActivity() {
    private lateinit var binding: ActivityMatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarLayout.toolbarTitle.text = "Match"
        binding.toolbarLayout.back.visibility = View.VISIBLE
        binding.toolbarLayout.back.setOnClickListener {
            onBackPressed()
        }
    }
}