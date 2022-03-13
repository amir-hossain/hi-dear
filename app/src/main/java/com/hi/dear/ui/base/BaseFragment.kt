package com.hi.dear.ui.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.hi.dear.R


abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {
    protected lateinit var binding: VB
    protected var viewModel: VM? = null
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = initViewModel()
        initProgressDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initViewBinding(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        attachObserver(viewModel)
        addLoadingObserver()
    }

    private fun addLoadingObserver() {
        if (viewModel == null) {
            return
        }

        if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).isLoading.observe(requireActivity(), Observer {
                initLoadingView(it)
            })
            (viewModel as BaseViewModel).isConnected.observe(requireActivity()) {
                showToast(R.string.no_internet_connection)
            }
        }
    }

    protected fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    protected fun showToast(msg: Int) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    protected abstract fun initViewBinding(inflater: LayoutInflater): VB

    protected abstract fun initViewModel(): VM?

    protected abstract fun initView()

    protected abstract fun attachObserver(viewModel: VM?)

    protected abstract fun initLoadingView(isLoading: Boolean)

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("please wait...")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setCancelable(false)
    }
}