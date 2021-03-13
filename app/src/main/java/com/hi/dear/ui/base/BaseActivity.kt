package com.hi.dear.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected var viewModel: VM? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = initViewBinding()
        viewModel = initViewModel()
        setContentView(binding.root)
        initViewModel()
        initView()
        attachObserver(viewModel)
        addLoadingObserver()
    }

    private fun addLoadingObserver() {
        if (viewModel == null) {
            return
        }

        if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).isLoading.observe(this, Observer {
                initLoadingView(it)
            })

        }
    }

    protected fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
    }

    protected abstract fun initViewBinding(): VB

    protected abstract fun initViewModel(): VM?

    protected abstract fun initView()

    protected abstract fun attachObserver(viewModel: VM?)

    protected abstract fun initLoadingView(isLoading: Boolean)
}