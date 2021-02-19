package com.hi.dear.ui.activity.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentManager
import com.hi.dear.R
import com.hi.dear.databinding.DialogGenderBinding
import com.hi.dear.ui.base.BaseDialog


class GenderDialog(private val listener: IGenderDialogListener) : BaseDialog() {
    private var selectedValue = ""
    private val TAG = "GenderDialog"
    private lateinit var binding: DialogGenderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {

        binding = DialogGenderBinding.inflate(inflater)

        binding.radGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radMale -> selectedValue = "male"
                else -> selectedValue = "female"
            }
        }

        binding.dlgGenderBtnCancel.setOnClickListener {
            closeDialog()
            if (listener != null) {
                listener!!.onNegativeBtnClicked()
            }
        }

        binding.dlgGenderBtnOk.setOnClickListener {
            closeDialog()
            if (listener != null) {
                listener.onPositiveBtnClicked(selectedValue)
            }
        }
        return binding.root
    }


    override fun showDialog(supportFragmentManager: FragmentManager?) {
        super.showDialog(supportFragmentManager, TAG)
    }

    interface IGenderDialogListener {
        fun onPositiveBtnClicked(value: String)
        fun onNegativeBtnClicked()
    }
}
