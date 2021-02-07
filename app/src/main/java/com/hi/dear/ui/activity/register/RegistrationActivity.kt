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
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.activity.login.ViewModelFactory
import com.hi.dear.ui.activity.login.afterTextChanged
import com.hi.dear.ui.base.BaseActivity

class RegistrationActivity : BaseActivity() {

    private val photo: String= ""
    private lateinit var registrationViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registrationViewModel = ViewModelProvider(
            this,
            ViewModelFactory(RegistrationRepository(LocalRegistrationSource(application)))
        )
            .get(RegisterViewModel::class.java)

        binding.id.afterTextChanged { checkValidity(binding) }

        binding.name.afterTextChanged { checkValidity(binding) }

        binding.password.afterTextChanged { checkValidity(binding) }

        binding.conPassword.apply {

            afterTextChanged { checkValidity(binding) }

            binding.register.setOnClickListener {
                binding.loading.visibility = View.VISIBLE
                binding.register.isEnabled = false
                registrationViewModel.register(
                    id = binding.id.text.toString(),
                    password = binding.password.text.toString(),
                    conPass = binding.conPassword.text.toString(),
                    name = binding.name.text.toString(),
                    photo = photo
                )
            }

            binding.login.setOnClickListener {
                startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
            }
        }

        registrationViewModel.registrationFormState.observe(this, Observer {
            val registrationState = it ?: return@Observer

            // disable login button unless both username / password is valid
            binding.register.isEnabled = registrationState.isDataValid

            if (registrationState.idError != null) {
                binding.id.error = getString(registrationState.idError)
            }
            if (registrationState.passwordError != null) {
                binding.password.error = getString(registrationState.passwordError)
            }

            if (registrationState.conPassError != null) {
                binding.conPassword.error = getString(registrationState.conPassError)
            }

            if (registrationState.nameError != null) {
                binding.name.error = getString(registrationState.nameError)
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
            id = binding.id.text.toString(),
            name = binding.name.text.toString(),
            password = binding.password.text.toString(),
            conPass = binding.conPassword.text.toString(),
        )
    }
}