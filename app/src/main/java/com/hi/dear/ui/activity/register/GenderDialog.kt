package com.hi.dear.ui.activity.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentManager
import com.hi.dear.R
import com.hi.dear.databinding.DialogGenderBinding
import com.hi.dear.ui.Constant
import com.hi.dear.ui.base.BaseDialog


class GenderDialog(
    private val listener: IGenderDialogListener,
    private var defaultValue: String? = null
) : BaseDialog() {
    private var selectedValue = ""
    private val TAG = "GenderDialog"
    private var binding: DialogGenderBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = DialogGenderBinding.inflate(inflater)

        setDefaultValue(defaultValue)

        binding?.radGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radMale -> selectedValue = "male"
                else -> selectedValue = "female"
            }
        }

        binding?.dlgGenderBtnCancel?.setOnClickListener {
            closeDialog()
            listener.onNegativeBtnClicked()
        }

        binding?.dlgGenderBtnOk?.setOnClickListener {
            closeDialog()
            listener.onPositiveBtnClicked(selectedValue)
        }
        return binding!!.root
    }

    fun setDefaultValue(defaultValue: String?) {
        /**
         *  extra check to prevent quick access link profileActivity attachObserver
         * */
        if (binding == null) {
            this.defaultValue = defaultValue
            return
        }
        val defaultId = getDefaultValueId(defaultValue)
        if (defaultId != null) {
            binding?.radGroup?.check(defaultId)
        }
    }

    private fun getDefaultValueId(defaultValue: String?): Int? {
        var defaultId: Int? = null
        if (defaultValue?.equals(Constant.male) == true) {
            defaultId = R.id.radMale
        } else if (defaultValue?.equals(Constant.female) == true) {
            defaultId = R.id.radFemale
        }
        return defaultId
    }


    override fun showDialog(supportFragmentManager: FragmentManager?) {
        super.showDialog(supportFragmentManager, TAG)
    }

    interface IGenderDialogListener {
        fun onPositiveBtnClicked(value: String)
        fun onNegativeBtnClicked()
    }
}
