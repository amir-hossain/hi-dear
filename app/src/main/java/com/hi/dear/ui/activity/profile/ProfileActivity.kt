package com.hi.dear.ui.activity.profile

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.ActivityProfileBinding
import com.hi.dear.repo.ProfileRepository
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.activity.register.GenderDialog
import com.hi.dear.ui.base.BaseActivity


class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileViewModel>(),
    GenderDialog.IGenderDialogListener {
    private lateinit var mode: Mode
    private var previousEditCloseBtn: View? = null
    private var genderDialog: GenderDialog? = null

    private val saveChangeListener = object : DialogFactory.ITwoBtnListener {
        override fun onPositiveBtnClicked() {
            onBackPressed()
        }

        override fun onNegativeBtnClicked() {

        }
    }

    enum class Mode {
        VIEW, EDIT
    }

    companion object {
        private const val Args = "args"
        private const val Args_Mode = "mode"
        fun start(context: Context, userData: UserCore?, mode: Mode) {
            if (userData == null) {
                return
            }
            var intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(Args, userData)
            intent.putExtra(Args_Mode, mode)
            context.startActivity(intent)
        }
    }

    override fun initViewBinding(): ActivityProfileBinding {
        return ActivityProfileBinding.inflate(layoutInflater)
    }


    override fun initView() {
        val userData = intent.getParcelableExtra<UserCore>(Args)!!
        mode = (intent.getSerializableExtra(Args_Mode) as Mode)
        binding.toolbarLayout.toolbarTitle.text = userData.name
        viewModel?.getProfileData(userData.id!!)

        binding.toolbarLayout.toolbarBtn.text = getText(R.string.save)
        binding.toolbarLayout.toolbarBtn.visibility = View.VISIBLE
        Utils.disableView(binding.toolbarLayout.toolbarBtn)

        binding.aboutMeField.setSelection(binding.aboutMeField.text.toString().length)
        initClickListener()

        genderDialog = GenderDialog(this)
        binding.genderField.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                genderDialog?.showDialog(supportFragmentManager)
            }
        }
    }

    private fun initClickListener() {
        binding.toolbarLayout.back.setOnClickListener {
            if (viewModel?.editState?.value == true) {
                DialogFactory.makeDialog(getString(R.string.save_change), saveChangeListener)
                    .showDialog(supportFragmentManager)
            } else {
                super.onBackPressed()
            }
        }
        binding.btnNameEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.nameField.valueTickBtn
            binding.nameField.editLabel.text = getString(R.string.name)
            binding.nameField.editText.setText(binding.name.text)
            setCursorTotheLast(binding.nameField.editText)
            hideView(binding.btnNameEdit, binding.name, binding.label1)
            binding.nameField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.nameField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnNameEdit, binding.name, binding.label1)
            binding.nameField.rootEditComponent.visibility = View.GONE
            val newValue = binding.nameField.editText.text.toString()
            viewModel?.fieldDataChanged(newName = newValue)
            binding.name.text = newValue
        }

        binding.btnAgeEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.ageField.valueTickBtn
            binding.ageField.editLabel.text = getString(R.string.age)
            binding.ageField.editText.setText(binding.age.text)
            setCursorTotheLast(binding.ageField.editText)
            hideView(binding.btnAgeEdit, binding.age, binding.label2)
            binding.ageField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.ageField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnAgeEdit, binding.age, binding.label2)
            binding.ageField.rootEditComponent.visibility = View.GONE
            val newValue = binding.ageField.editText.text.toString()
            viewModel?.fieldDataChanged(newAge = newValue)
            binding.age.text = newValue
        }

        binding.btnCountryEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.countryField.valueTickBtn
            binding.countryField.editLabel.text = getString(R.string.country)
            binding.countryField.editText.setText(binding.country.text)
            setCursorTotheLast(binding.countryField.editText)
            hideView(binding.btnCountryEdit, binding.country, binding.label3)
            binding.countryField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.countryField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnCountryEdit, binding.country, binding.label3)
            binding.countryField.rootEditComponent.visibility = View.GONE
            val newValue = binding.countryField.editText.text.toString()
            viewModel?.fieldDataChanged(newCountry = newValue)
            binding.country.text = newValue
        }

        binding.btnCityEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.cityField.valueTickBtn
            binding.cityField.editLabel.text = getString(R.string.city)
            binding.cityField.editText.setText(binding.city.text)
            setCursorTotheLast(binding.cityField.editText)
            hideView(binding.btnCityEdit, binding.city, binding.label4)
            binding.cityField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.cityField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnCityEdit, binding.city, binding.label4)
            binding.cityField.rootEditComponent.visibility = View.GONE
            val newValue = binding.cityField.editText.text.toString()
            viewModel?.fieldDataChanged(newCity = newValue)
            binding.city.text = newValue
        }

        binding.btnGenderEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.genderField.valueTickBtn
            binding.genderField.editLabel.text = getString(R.string.gender)
            binding.genderField.editText.setText(binding.gender.text)
            setCursorTotheLast(binding.genderField.editText)
            hideView(binding.btnGenderEdit, binding.gender, binding.label5)
            binding.genderField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.genderField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnGenderEdit, binding.gender, binding.label5)
            binding.genderField.rootEditComponent.visibility = View.GONE
            val newValue = binding.genderField.editText.text.toString()
            viewModel?.fieldDataChanged(newGender = newValue)
            binding.gender.text = newValue
        }

        binding.btnAboutMeEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.aboutTickBtn
            binding.aboutMeField.setText(binding.aboutMe.text)
            setCursorTotheLast(binding.aboutMeField)
            binding.aboutMe.visibility = View.GONE
            binding.aboutTickBtn.visibility = View.VISIBLE
            binding.aboutMeField.visibility = View.VISIBLE
        }

        binding.aboutTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            binding.aboutMe.visibility = View.VISIBLE
            binding.aboutTickBtn.visibility = View.GONE
            binding.aboutMeField.visibility = View.GONE
            val newValue = binding.aboutMeField.text.toString()
            viewModel?.fieldDataChanged(newAbout = newValue)
            binding.aboutMe.text = newValue
        }

        binding.toolbarLayout.toolbarBtn.setOnClickListener {
            viewModel?.saveEditedData()
        }
    }

    private fun setCursorTotheLast(editText: EditText) {
        editText.setSelection(editText.text.toString().length)
        editText.requestFocus()
    }

    private fun hideView(view1: View, view2: View, view3: View) {
        view1.visibility = View.GONE
        view2.visibility = View.GONE
        view3.visibility = View.GONE
    }

    private fun showView(view1: View, view2: View, view3: View) {
        view1.visibility = View.VISIBLE
        view2.visibility = View.VISIBLE
        view3.visibility = View.VISIBLE
    }

    override fun attachObserver(viewModel: ProfileViewModel?) {
        viewModel?.profileResult?.observe(this@ProfileActivity, Observer {
            val result = it ?: return@Observer

            if (result.success) {
                showData(result.data!!)
                storePreviousValue(result.data)
                if (mode == Mode.EDIT) {
                    showEditIcon()
                }
                genderDialog?.setDefaultValue(result.data.gender)
            } else {
                showToast(result.msg)
            }
        })

        viewModel?.editState?.observe(this@ProfileActivity, Observer {
            val result = it ?: return@Observer

            if (result) {
                Utils.enableView(binding.toolbarLayout.toolbarBtn)
            } else {
                Utils.disableView(binding.toolbarLayout.toolbarBtn)
            }
        })

        viewModel?.saveResult?.observe(this@ProfileActivity, Observer {
            val result = it ?: return@Observer
            showToast(result.msg)
        })
    }

    private fun storePreviousValue(data: ProfileData) {
        viewModel?.previousName = data.name!!
        viewModel?.previousAge = data.age!!
        viewModel?.previousGender = data.gender!!
        viewModel?.previousCountry = data.country!!
        viewModel?.previousCity = data.city!!
        viewModel?.previousAbout = data.about!!
    }

    private fun showEditIcon() {
        binding.btnNameEdit.visibility = View.VISIBLE
        binding.btnAgeEdit.visibility = View.VISIBLE
        binding.btnGenderEdit.visibility = View.VISIBLE
        binding.btnCountryEdit.visibility = View.VISIBLE
        binding.btnCityEdit.visibility = View.VISIBLE
        binding.btnAboutMeEdit.visibility = View.VISIBLE
    }

    private fun showData(data: ProfileData) {
        Glide.with(this)
            .load(data.picture)
            .into(binding.img1)

        binding.name.text = data.name
        binding.age.text = data.age
        binding.gender.text = data.gender
        binding.country.text = data.country
        binding.city.text = data.city
        binding.aboutMe.text = data.about
    }

    override fun initViewModel(): ProfileViewModel? {
        return ViewModelProvider(
            this, ViewModelFactory(ProfileRepository())
        ).get(ProfileViewModel::class.java)
    }

    override fun initLoadingView(isLoading: Boolean) {

    }

    override fun onPositiveBtnClicked(value: String) {
        binding.genderField.editText.clearFocus()
        binding.genderField.editText.setText(value)
    }

    override fun onNegativeBtnClicked() {
        binding.genderField.editText.clearFocus()
    }
}