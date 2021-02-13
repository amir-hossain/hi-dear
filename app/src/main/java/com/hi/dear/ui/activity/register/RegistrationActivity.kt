package com.hi.dear.ui.activity.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.databinding.ActivityRegistrationBinding
import com.hi.dear.repo.RegistrationRepository
import com.hi.dear.source.local.LocalRegistrationSource
import com.hi.dear.ui.GenderDialog
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.activity.login.ViewModelFactory
import com.hi.dear.ui.activity.login.afterTextChanged
import com.hi.dear.ui.base.BaseActivity

class RegistrationActivity : BaseActivity(), GenderDialog.IGenderDialogListener {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var genderDialog: GenderDialog
    private lateinit var registrationViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        genderDialog = GenderDialog(this)
        registrationViewModel = ViewModelProvider(
            this,
            ViewModelFactory(RegistrationRepository(LocalRegistrationSource(application)))
        )
            .get(RegisterViewModel::class.java)

        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.userName.afterTextChanged { checkValidity(binding) }

        binding.age.afterTextChanged { checkValidity(binding) }

        binding.country.afterTextChanged { checkValidity(binding) }

        binding.city.afterTextChanged { checkValidity(binding) }

        binding.emailOrMobile.afterTextChanged { checkValidity(binding) }

        binding.gender.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                genderDialog.showDialog(supportFragmentManager)
            }
        }

        binding.password.apply {

            afterTextChanged { checkValidity(binding) }

            binding.signUpBtn.setOnClickListener {
                binding.loading.visibility = View.VISIBLE
                binding.signUpBtn.isEnabled = false
                registrationViewModel.register(
                    userName = binding.userName.text.toString(),
                    password = binding.password.text.toString(),
                    age = binding.age.text.toString(),
                    gender = binding.gender.text.toString(),
                    city = binding.city.text.toString(),
                    county = binding.country.text.toString(),
                    emailOrMobile = binding.emailOrMobile.text.toString()
                )
            }

            binding.signUpBtn.setOnClickListener {
                startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
                finish()
            }
        }

        registrationViewModel.registrationFormState.observe(this, Observer {
            val registrationState = it ?: return@Observer

            binding.signUpBtn.isEnabled = registrationState.isDataValid

            if (registrationState.userNameError != null) {
                binding.userName.error = getString(registrationState.userNameError)
            }

            if (registrationState.ageError != null) {
                binding.age.error = getString(registrationState.ageError)
            }
            if (registrationState.genderError != null) {
                binding.gender.error = getString(registrationState.genderError)
            } else {
                binding.gender.error = null
            }

            if (registrationState.emailOrMobileError != null) {
                binding.emailOrMobile.error = getString(registrationState.emailOrMobileError)
            }
            if (registrationState.passwordError != null) {
                binding.password.error = getString(registrationState.passwordError)
            }

            if (registrationState.cityError != null) {
                binding.city.error = getString(registrationState.cityError)
            }

            if (registrationState.countryError != null) {
                binding.country.error = getString(registrationState.countryError)
            }
        })

        registrationViewModel.registrationResult.observe(this, Observer {
            val registrationResult = it ?: return@Observer

            binding.loading.visibility = View.GONE
            if(it.success){
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
            showToast(getString(registrationResult.msg))
            setResult(Activity.RESULT_OK)
        })
    }

    private fun checkValidity(binding: ActivityRegistrationBinding) {
        registrationViewModel.registerDataChanged(
            userName = binding.userName.text.toString(),
            age = binding.age.text.toString(),
            password = binding.password.text.toString(),
            emailOrMobile = binding.emailOrMobile.text.toString(),
            gender = binding.gender.text.toString(),
            city = binding.city.text.toString(),
            country = binding.country.text.toString(),
        )
    }

    override fun onPositiveBtnClicked(value: String) {
        binding.gender.clearFocus()
        binding.gender.setText(value)
        checkValidity(binding)
    }

    override fun onNegativeBtnClicked() {
        binding.gender.clearFocus()
        checkValidity(binding)
    }
}