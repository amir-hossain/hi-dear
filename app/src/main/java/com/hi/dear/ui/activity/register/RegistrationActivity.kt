package com.hi.dear.ui.activity.register

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hi.dear.databinding.ActivityRegistrationBinding
import com.hi.dear.repo.IRegistrationRepository
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.base.BaseActivity

class RegistrationActivity : BaseActivity<ActivityRegistrationBinding, RegisterViewModel>(),
    GenderDialog.IGenderDialogListener {

    private lateinit var genderDialog: GenderDialog

    override fun initView() {
        genderDialog = GenderDialog(this)

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
            }

            binding.signUpBtn.setOnClickListener {
                viewModel?.register(
                    userName = binding.userName.text.toString(),
                    password = binding.password.text.toString(),
                    age = binding.age.text.toString(),
                    gender = binding.gender.text.toString(),
                    city = binding.city.text.toString(),
                    county = binding.country.text.toString(),
                    emailOrMobile = binding.emailOrMobile.text.toString()
                )
            }
        }
    }

    private fun checkValidity(binding: ActivityRegistrationBinding) {
        viewModel?.registerDataChanged(
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

    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    override fun initViewBinding(): ActivityRegistrationBinding {
        return ActivityRegistrationBinding.inflate(layoutInflater)
    }

    override fun initViewModel(): RegisterViewModel {
        return ViewModelProvider(this, ViewModelFactory(IRegistrationRepository()))
            .get(RegisterViewModel::class.java)
    }

    override fun attachObserver(viewModel: RegisterViewModel?) {
        viewModel?.registrationFormState?.observe(this, Observer {
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

        viewModel?.registrationResult?.observe(this, Observer {
            val registrationResult = it ?: return@Observer

            binding.loading.visibility = View.GONE
            if (it.success) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
            showToast(getString(registrationResult.msg))
            setResult(Activity.RESULT_OK)
        })
    }

    override fun initLoadingView(isLoading: Boolean) {
        if (isLoading) {
            binding.loading.visibility = View.VISIBLE
            Utils.disableView(binding.signUpBtn)
        } else {
            binding.loading.visibility = View.GONE
            Utils.enableView(binding.signUpBtn)
        }
    }
}