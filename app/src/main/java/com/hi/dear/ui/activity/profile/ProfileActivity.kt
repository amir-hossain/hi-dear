package com.hi.dear.ui.activity.profile

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.ActivityProfileBinding
import com.hi.dear.repo.ProfileRepository
import com.hi.dear.ui.Constant.GALLERY_REQUEST_CODE
import com.hi.dear.ui.Constant.IMAGE_MIME_TYPE
import com.hi.dear.ui.DialogFactory
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ViewModelFactory
import com.hi.dear.ui.activity.profile.model.Img
import com.hi.dear.ui.activity.register.GenderDialog
import com.hi.dear.ui.base.BaseActivity
import permissions.dispatcher.*
import timber.log.Timber
import java.io.File

@RuntimePermissions
class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileViewModel>(),
    GenderDialog.IGenderDialogListener, DialogFactory.ISingleBtnListener {
    private var clickedImgPosition = 0
    private lateinit var mode: Mode
    private var previousEditCloseBtn: View? = null
    private var genderDialog: GenderDialog? = null
    private lateinit var currentRequest: PermissionRequest
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
        initBackButtonClickListener()
        initNameEditClickListener()
        initNameTickClickListener()
        initAgeEditClickListener()
        initAgeTickClickListener()
        initCountryEditClickListener()
        initCountryTickClickListener()
        initCityEditClickListener()
        initCityTickClickListener()
        initGenderEditClickListener()
        initGenderTickClickListener()
        initAboutMeEditClickListener()
        initAboutMeTickClickListener()

        binding.toolbarLayout.toolbarBtn.setOnClickListener {
            viewModel?.saveEditedData()
        }

        if (mode == Mode.EDIT) {
            initImgClickListenerFor(binding.layout1.root, position = 0)
            initImgClickListenerFor(binding.layout2.root, position = 1)
            initImgClickListenerFor(binding.layout3.root, position = 2)
            initImgClickListenerFor(binding.layout4.root, position = 3)
            initImgClickListenerFor(binding.layout5.root, position = 4)
            initImgClickListenerFor(binding.layout6.root, position = 5)

            initImgCloseClickListener(binding.layout1.btnClose, position = 0)
            initImgCloseClickListener(binding.layout2.btnClose, position = 1)
            initImgCloseClickListener(binding.layout3.btnClose, position = 2)
            initImgCloseClickListener(binding.layout4.btnClose, position = 3)
            initImgCloseClickListener(binding.layout5.btnClose, position = 4)
            initImgCloseClickListener(binding.layout6.btnClose, position = 5)
        }
    }

    private fun initImgCloseClickListener(view: View, position: Int) {
        view.setOnClickListener {
            clearPictureInPosition(position)
            showPictureList()
            view.visibility = View.GONE
        }
    }

    private fun initAboutMeTickClickListener() {
        binding.aboutTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            binding.aboutMe.visibility = View.VISIBLE
            binding.aboutTickBtn.visibility = View.GONE
            binding.aboutMeField.visibility = View.GONE
            val newValue = binding.aboutMeField.text.toString()
            viewModel?.fieldDataChanged(newAbout = newValue)
            binding.aboutMe.text = newValue
        }
    }

    private fun initAboutMeEditClickListener() {
        binding.btnAboutMeEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.aboutTickBtn
            binding.aboutMeField.setText(binding.aboutMe.text)
            setCursorToTheLast(binding.aboutMeField)
            binding.aboutMe.visibility = View.GONE
            binding.aboutTickBtn.visibility = View.VISIBLE
            binding.aboutMeField.visibility = View.VISIBLE
            binding.aboutMeField.requestFocus()
        }
    }

    private fun initGenderTickClickListener() {
        binding.genderField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnGenderEdit, binding.gender, binding.label5)
            binding.genderField.rootEditComponent.visibility = View.GONE
            val newValue = binding.genderField.editText.text.toString()
            viewModel?.fieldDataChanged(newGender = newValue)
            binding.gender.text = newValue
        }
    }

    private fun initGenderEditClickListener() {
        binding.btnGenderEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.genderField.valueTickBtn
            binding.genderField.editLabel.text = getString(R.string.gender)
            binding.genderField.editText.setText(binding.gender.text)
            setCursorToTheLast(binding.genderField.editText)
            hideView(binding.btnGenderEdit, binding.gender, binding.label5)
            binding.genderField.rootEditComponent.visibility = View.VISIBLE
        }
    }

    private fun initCityTickClickListener() {
        binding.cityField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnCityEdit, binding.city, binding.label4)
            binding.cityField.rootEditComponent.visibility = View.GONE
            val newValue = binding.cityField.editText.text.toString()
            viewModel?.fieldDataChanged(newCity = newValue)
            binding.city.text = newValue
        }
    }

    private fun initCityEditClickListener() {
        binding.btnCityEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.cityField.valueTickBtn
            binding.cityField.editLabel.text = getString(R.string.city)
            binding.cityField.editText.setText(binding.city.text)
            setCursorToTheLast(binding.cityField.editText)
            hideView(binding.btnCityEdit, binding.city, binding.label4)
            binding.cityField.rootEditComponent.visibility = View.VISIBLE
        }
    }

    private fun initCountryTickClickListener() {
        binding.countryField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnCountryEdit, binding.country, binding.label3)
            binding.countryField.rootEditComponent.visibility = View.GONE
            val newValue = binding.countryField.editText.text.toString()
            viewModel?.fieldDataChanged(newCountry = newValue)
            binding.country.text = newValue
        }
    }

    private fun initCountryEditClickListener() {
        binding.btnCountryEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.countryField.valueTickBtn
            binding.countryField.editLabel.text = getString(R.string.country)
            binding.countryField.editText.setText(binding.country.text)
            setCursorToTheLast(binding.countryField.editText)
            hideView(binding.btnCountryEdit, binding.country, binding.label3)
            binding.countryField.rootEditComponent.visibility = View.VISIBLE
        }
    }

    private fun initAgeTickClickListener() {
        binding.ageField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnAgeEdit, binding.age, binding.label2)
            binding.ageField.rootEditComponent.visibility = View.GONE
            val newValue = binding.ageField.editText.text.toString()
            viewModel?.fieldDataChanged(newAge = newValue)
            binding.age.text = newValue
        }
    }

    private fun initAgeEditClickListener() {
        binding.btnAgeEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.ageField.valueTickBtn
            binding.ageField.editLabel.text = getString(R.string.age)
            binding.ageField.editText.setText(binding.age.text)
            setCursorToTheLast(binding.ageField.editText)
            hideView(binding.btnAgeEdit, binding.age, binding.label2)
            binding.ageField.rootEditComponent.visibility = View.VISIBLE
        }
    }

    private fun initNameTickClickListener() {
        binding.nameField.valueTickBtn.setOnClickListener {
            previousEditCloseBtn = null
            showView(binding.btnNameEdit, binding.name, binding.label1)
            binding.nameField.rootEditComponent.visibility = View.GONE
            val newValue = binding.nameField.editText.text.toString()
            viewModel?.fieldDataChanged(newName = newValue)
            binding.name.text = newValue
        }
    }

    private fun initNameEditClickListener() {
        binding.btnNameEdit.setOnClickListener {
            previousEditCloseBtn?.performClick()
            previousEditCloseBtn = binding.nameField.valueTickBtn
            binding.nameField.editLabel.text = getString(R.string.name)
            binding.nameField.editText.setText(binding.name.text)
            setCursorToTheLast(binding.nameField.editText)
            hideView(binding.btnNameEdit, binding.name, binding.label1)
            binding.nameField.rootEditComponent.visibility = View.VISIBLE
        }
    }

    private fun initBackButtonClickListener() {
        binding.toolbarLayout.back.setOnClickListener {
            if (viewModel?.editState?.value == true) {
                DialogFactory.makeDialog(getString(R.string.save_change), saveChangeListener)
                    .showDialog(supportFragmentManager)
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun initImgClickListenerFor(view: View, position: Int) {
        view.setOnClickListener {
            clickedImgPosition = position
            showGalleryWithPermissionCheck()
        }
    }

    private fun setCursorToTheLast(editText: EditText) {
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
                if (mode == Mode.EDIT) {
                    storePreviousValue(result.data)
                    showEditButtons()
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

    private fun getImgListFrom(picList: MutableList<String>): MutableList<Img> {
        val list = mutableListOf<Img>()
        for (index in picList.indices) {
            list.add(Img(path = picList[index], position = index))
        }
        return list
    }

    private fun storePreviousValue(data: ProfileData) {
        viewModel?.previousImgList = data.picList!!
        viewModel?.previousName = data.name!!
        viewModel?.previousAge = data.age!!
        viewModel?.previousGender = data.gender!!
        viewModel?.previousCountry = data.country!!
        viewModel?.previousCity = data.city!!
        viewModel?.previousAbout = data.about!!
    }

    private fun showEditButtons() {
        binding.btnNameEdit.visibility = View.VISIBLE
        binding.btnAgeEdit.visibility = View.VISIBLE
        binding.btnGenderEdit.visibility = View.VISIBLE
        binding.btnCountryEdit.visibility = View.VISIBLE
        binding.btnCityEdit.visibility = View.VISIBLE
        binding.btnAboutMeEdit.visibility = View.VISIBLE
    }

    private fun showData(data: ProfileData) {
        showPictureList()
        binding.name.text = data.name
        binding.age.text = data.age
        binding.gender.text = data.gender
        binding.country.text = data.country
        binding.city.text = data.city
        binding.aboutMe.text = data.about
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showGallery() {
        val galleryIntent = Intent()
        galleryIntent.type = IMAGE_MIME_TYPE
        galleryIntent.action = Intent.ACTION_PICK
        open(galleryIntent)
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
        DialogFactory.makeDialog(getString(R.string.gallery_permission_explanation), this)
            .showDialog(supportFragmentManager)
    }

    private fun open(intent: Intent) {
        try {
            startActivityForResult(
                Intent.createChooser(intent, getString(R.string.choose_img)),
                GALLERY_REQUEST_CODE
            )
        } catch (e: ActivityNotFoundException) {
            showToast(R.string.gallery_app_not_found)
        }
    }

    private fun showAddMoreButton() {
        if (mode != Mode.EDIT) {
            return
        } else if (viewModel?.previousName.size == 0) {
            showStaticPicture(binding.layout1.img, R.drawable.ic_plus)
        } else {
            when (previewImgList.size) {
                1 -> {
                    showStaticPicture(binding.layout2.img, R.drawable.ic_plus)
                    makeViewVisible(binding.layout2.root)
                }
                2 -> {
                    showStaticPicture(binding.layout3.img, R.drawable.ic_plus)
                    makeViewVisible(binding.layout3.root)
                }
                3 -> {
                    showStaticPicture(binding.layout4.img, R.drawable.ic_plus)
                    makeViewVisible(binding.layout4.root)
                }
                4 -> {
                    showStaticPicture(binding.layout5.img, R.drawable.ic_plus)
                    makeViewVisible(binding.layout5.root)
                }
                5 -> {
                    showStaticPicture(binding.layout6.img, R.drawable.ic_plus)
                    makeViewVisible(binding.layout6.root)
                }
            }
        }
    }


    private fun clearPictureInPosition(position: Int) {
        var img: Img? = null
        for (data in previewImgList) {
            if (data.position == position) {
                img = data
            }
        }

        /* to avoid concurrent modification*/
        if (img != null) {
            previewImgList.remove(img)
        }
        viewModel?.editState?.value = previewImgList.isNotEmpty()
    }

    private fun showPictureList() {
        for (index in previewImgList.indices) {
            val pic = previewImgList[index].path
            val imageView = getImageViewFor(index)
            Glide.with(this)
                .load(pic)
                .into(imageView)
        }
        showAddMoreButton()
    }

    private fun showStaticPicture(imageView: ImageView, @DrawableRes pic: Int) {
        Glide.with(this)
            .load(pic)
            .into(imageView)
    }

    private fun getImageViewFor(position: Int): ImageView {
        val imageView: ImageView
        when (position) {
            0 -> {
                imageView = binding.layout1.img
                makeViewVisible(imageView)
                showCloseButtonVisible(binding.layout1.btnClose)
            }
            1 -> {
                imageView = binding.layout2.img
                makeViewVisible(imageView)
                showCloseButtonVisible(binding.layout2.btnClose)
            }
            2 -> {
                imageView = binding.layout3.img
                makeViewVisible(imageView)
                showCloseButtonVisible(binding.layout3.btnClose)
            }
            3 -> {
                imageView = binding.layout4.img
                makeViewVisible(imageView)
                showCloseButtonVisible(binding.layout4.btnClose)
            }
            4 -> {
                imageView = binding.layout5.img
                makeViewVisible(imageView)
                showCloseButtonVisible(binding.layout5.btnClose)
            }
            else -> {
                imageView = binding.layout6.img
                makeViewVisible(imageView)
                showCloseButtonVisible(binding.layout6.btnClose)
            }
        }
        return imageView
    }

    private fun showCloseButtonVisible(btnClose: ImageView) {
        if (mode == Mode.EDIT && !btnClose.isVisible) {
            btnClose.visibility = View.VISIBLE
        }
    }

    private fun makeViewVisible(view: View) {
        if (!view.isVisible) {
            view.visibility = View.VISIBLE
        }
    }

    override fun initViewModel(): ProfileViewModel {
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
                val pic = getImageFrom(cursor)
                if (pic != null) {
                    if (clickedImgPosition < previewImgList.size) {
                        previewImgList.removeAt(clickedImgPosition)
                    }
                    previewImgList.add(clickedImgPosition, Img(clickedImgPosition, pic.path))
                    viewModel?.editState?.value = true
                    showPictureList()
                }
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