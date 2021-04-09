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
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.chat.ChatActivity
import com.hi.dear.ui.base.BaseActivity


class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileViewModel>() {

    private var previousAboutValue = ""
    private var aboutMeChanged = false
    private var previousGenderValue = ""
    private var genderChanged = false
    private var previousCityValue = ""
    private var cityChanged = false
    private var previousCountryValue = ""
    private var countryChanged = false
    private var previousAgeValue = ""
    private var ageChanged = false
    private var nameChanged = false
    private var previousNameValue = ""
    private var previousEditCloseBtn: View? = null

    override fun initViewBinding(): ActivityProfileBinding {
        return ActivityProfileBinding.inflate(layoutInflater)
    }


    override fun initView() {
        val userData = intent.getParcelableExtra<UserCore>(ChatActivity.Args)!!
        binding.toolbarLayout.toolbarTitle.text = userData.name
        viewModel?.getProfileData(userData.id!!)

        binding.aboutMeField.setSelection(binding.aboutMeField.text.toString().length)
        initClickListener()
    }

    private fun initClickListener() {
        binding.toolbarLayout.back.setOnClickListener {
            super.onBackPressed()
        }
        binding.btnNameEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.nameField.valueTickBtn
            binding.nameField.editLabel.text = getString(R.string.name)
            previousNameValue = binding.name.text.toString()
            binding.nameField.editText.setText(previousNameValue)
            setCursorTotheLast(binding.nameField.editText)
            hideView(binding.btnNameEdit, binding.name, binding.label1)
            binding.nameField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.nameField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnNameEdit, binding.name, binding.label1)
            binding.nameField.rootEditComponent.visibility = View.GONE
            val newValue = binding.nameField.editText.text.toString()
            nameChanged = hasValueChanged(previousNameValue, newValue)
            if (nameChanged) {
                binding.name.text = newValue
            }
        }

        binding.btnAgeEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.ageField.valueTickBtn
            binding.ageField.editLabel.text = getString(R.string.age)
            previousAgeValue = binding.age.text.toString()
            binding.ageField.editText.setText(previousAgeValue)
            setCursorTotheLast(binding.ageField.editText)
            hideView(binding.btnAgeEdit, binding.age, binding.label2)
            binding.ageField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.ageField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnAgeEdit, binding.age, binding.label2)
            binding.ageField.rootEditComponent.visibility = View.GONE
            val newValue = binding.ageField.editText.text.toString()
            ageChanged = hasValueChanged(previousAgeValue, newValue)
            if (ageChanged) {
                binding.age.text = newValue
            }
        }

        binding.btnCountryEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.countryField.valueTickBtn
            binding.countryField.editLabel.text = getString(R.string.country)
            previousCountryValue = binding.country.text.toString()
            binding.countryField.editText.setText(previousCountryValue)
            setCursorTotheLast(binding.countryField.editText)
            hideView(binding.btnCountryEdit, binding.country, binding.label3)
            binding.countryField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.countryField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnCountryEdit, binding.country, binding.label3)
            binding.countryField.rootEditComponent.visibility = View.GONE
            val newValue = binding.countryField.editText.text.toString()
            countryChanged = hasValueChanged(previousCountryValue, newValue)
            if (countryChanged) {
                binding.country.text = newValue
            }
        }

        binding.btnCityEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.cityField.valueTickBtn
            binding.cityField.editLabel.text = getString(R.string.city)
            previousCityValue = binding.city.text.toString()
            binding.cityField.editText.setText(previousCityValue)
            setCursorTotheLast(binding.cityField.editText)
            hideView(binding.btnCityEdit, binding.city, binding.label4)
            binding.cityField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.cityField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnCityEdit, binding.city, binding.label4)
            binding.cityField.rootEditComponent.visibility = View.GONE
            val newValue = binding.cityField.editText.text.toString()
            cityChanged = hasValueChanged(previousCityValue, newValue)
            if (cityChanged) {
                binding.city.text = newValue
            }
        }

        binding.btnGenderEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.genderField.valueTickBtn
            binding.genderField.editLabel.text = getString(R.string.gender)
            previousGenderValue = binding.gender.text.toString()
            binding.genderField.editText.setText(previousGenderValue)
            setCursorTotheLast(binding.genderField.editText)
            hideView(binding.btnGenderEdit, binding.gender, binding.label5)
            binding.genderField.rootEditComponent.visibility = View.VISIBLE
        }

        binding.genderField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnGenderEdit, binding.gender, binding.label5)
            binding.genderField.rootEditComponent.visibility = View.GONE
            val newValue = binding.genderField.editText.text.toString()
            genderChanged = hasValueChanged(previousGenderValue, newValue)
            if (genderChanged) {
                binding.gender.text = newValue
            }
        }

        binding.btnAboutMeEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.aboutTickBtn
            previousAboutValue = binding.aboutMe.text.toString()
            binding.aboutMeField.setText(previousAboutValue)
            setCursorTotheLast(binding.aboutMeField)
            binding.btnAgeEdit.visibility = View.GONE
            binding.aboutMe.visibility = View.GONE
            binding.aboutTickBtn.visibility = View.VISIBLE
            binding.aboutMeField.visibility = View.VISIBLE
        }

        binding.aboutTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            binding.btnAgeEdit.visibility = View.VISIBLE
            binding.aboutMe.visibility = View.VISIBLE
            binding.aboutTickBtn.visibility = View.GONE
            binding.aboutMeField.visibility = View.GONE

            val newValue = binding.aboutMeField.text.toString()
            aboutMeChanged = hasValueChanged(previousAboutValue, newValue)
            if (aboutMeChanged) {
                binding.aboutMe.text = newValue
            }
        }
    }

    private fun hasValueChanged(previousValue: String, newValue: String): Boolean {
        if (newValue.isBlank()) {
            return false
        }
        return previousValue != newValue
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
                showEditIcon()
            } else {
                showToast(result.msg)
            }
        })
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

    companion object {
        const val Args = "args"
        fun start(context: Context, userData: UserCore?) {
            if (userData == null) {
                return
            }
            var intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(Args, userData)
            context.startActivity(intent)
        }
    }
}