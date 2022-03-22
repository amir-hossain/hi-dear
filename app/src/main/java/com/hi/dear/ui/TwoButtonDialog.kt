package com.hi.dear.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import com.hi.dear.databinding.DialogTwoButtonBinding
import com.hi.dear.ui.base.BaseDialog


class  TwoButtonDialog private constructor(): BaseDialog(){
    private lateinit var binding: DialogTwoButtonBinding
    private var listener: DialogFactory.ITwoBtnListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        binding = DialogTwoButtonBinding.inflate(inflater)

          binding.dlgTwoButtonBtnOk.setOnClickListener {
              closeDialog()
              if (listener != null) {
                  listener!!.onPositiveBtnClicked()
              }
          }

        binding.dlgTwoButtonBtnCancel.setOnClickListener {
            closeDialog()
            if (listener != null) {
                listener!!.onNegativeBtnClicked()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val messageRes: String? = arguments?.getString(ARG_MESSAGE)

        if (messageRes != null) {
            binding.dlgTwoButtonTvMessage.text = messageRes
        }
    }


    companion object {
        const val TAG = "OneButtonDialogTag"
        fun newInstance(
            messageRes: String,
            listener: DialogFactory.ITwoBtnListener
        ): TwoButtonDialog {
            val twoButtonDialog = TwoButtonDialog()
            twoButtonDialog.listener = listener
            val args = Bundle()
            args.putString(ARG_MESSAGE, messageRes)
            twoButtonDialog.arguments = args
            return twoButtonDialog
        }
    }

    override fun showDialog(supportFragmentManager: FragmentManager?){
       super.showDialog(supportFragmentManager, TAG)
    }
}
