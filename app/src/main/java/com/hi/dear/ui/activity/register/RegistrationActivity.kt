package com.hi.dear.ui.activity.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.databinding.ActivityRegistrationBinding
import com.hi.dear.repo.RegistrationRepository
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.login.LoginActivity
import com.hi.dear.ui.base.BaseActivity
import permissions.dispatcher.*
import timber.log.Timber
import java.io.File


@RuntimePermissions
class RegistrationActivity : BaseActivity<ActivityRegistrationBinding, RegisterViewModel>(),
    GenderDialog.IGenderDialogListener, DialogFactory.ISingleBtnListener {

    private var pic: File? = null
    private lateinit var currentRequest: PermissionRequest
    private val GALLERY_REQUEST_CODE = 420
    private val IMAGE_MIME_TYPE = "image/*";
    private lateinit var genderDialog: GenderDialog

    override fun initView() {
        Utils.disableView(binding.signUpBtn)
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
                viewModel?.register(
                    userName = binding.userName.text.toString(),
                    password = binding.password.text.toString(),
                    age = binding.age.text.toString(),
                    gender = binding.gender.text.toString(),
                    city = binding.city.text.toString(),
                    county = binding.country.text.toString(),
                    emailOrMobile = binding.emailOrMobile.text.toString(),
                    picture = pic!!
                )
            }
        }

        binding.addBtn.setOnClickListener {
            showGalleryWithPermissionCheck()
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
            picture = pic
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
        return ViewModelProvider(this, ViewModelFactory(RegistrationRepository()))
            .get(RegisterViewModel::class.java)
    }

    override fun attachObserver(viewModel: RegisterViewModel?) {
        viewModel?.registrationFormState?.observe(this, Observer {
            val registrationState = it ?: return@Observer

            if (registrationState.isDataValid) {
                Utils.enableView(binding.signUpBtn)
            } else {
                Utils.disableView(binding.signUpBtn)
            }

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
            if (registrationState.pictureError != null) {
                binding.addBtn.error = getString(registrationState.pictureError)
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

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showGallery() {
        val intent = Intent()
        intent.type = IMAGE_MIME_TYPE
        intent.action = Intent.ACTION_PICK

        startActivityForResult(Intent.createChooser(intent, "Chose Image"), GALLERY_REQUEST_CODE)
        Timber.i("showGallery called")
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onGalleryDenied() {
        Timber.i("OnPermissionDenied called")
        showToast(R.string.permission_gallery_denied)
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onGalleryNeverAskAgain() {
        Timber.i("OnNeverAskAgain called")
        showToast(R.string.permission_gallery_denied_permanent)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showRationaleForGallery(request: PermissionRequest) {
        Timber.i("showRationaleForContacts called")
        currentRequest = request
        DialogFactory.makeDialog(R.string.gallery_permission_explanation, this)
            .showDialog(supportFragmentManager)
    }

    override fun onPositiveBtnClicked() {
        currentRequest.proceed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (data != null && data.data != null) {
                val cursor: Cursor? = contentResolver.query(
                    data.data!!, null,
                    null, null, null
                )

                pic = getImageFrom(cursor)

                Glide.with(this)
                    .load(pic)
                    .into(binding.profileImage)
                checkValidity(binding)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun getImageFrom(cursor: Cursor?): File? {
        if (cursor == null) {
            return null
        }
        val columnName = MediaStore.Images.Media.DATA
        if (cursor.moveToFirst()) {
            val filePath = cursor.getString(cursor.getColumnIndexOrThrow(columnName))
            cursor.close()
            return File(filePath)
        }
        cursor.close()
        return null
    }
}