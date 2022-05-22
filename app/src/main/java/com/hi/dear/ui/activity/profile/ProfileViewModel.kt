package com.hi.dear.ui.activity.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.repo.ProfileRepository
import com.hi.dear.ui.App
import com.hi.dear.ui.Utils
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: ProfileRepository) : BaseViewModel() {

    private var imgChanged = false
    private var nameChanged = false
    private var ageChanged = false
    private var countryChanged = false
    private var cityChanged = false
    private var genderChanged = false
    private var aboutChanged = false

    private lateinit var newImgList: MutableList<String>
    var previousAbout = ""
    var previousGender = ""
    var previousCity = ""
    var previousCountry = ""
    var previousAge = ""
    var previousName = ""
    var previousImgList = mutableListOf<String>()

    private var newAbout: String? = null
    private var newGender: String? = null
    private var newCity: String? = null
    private var newCountry: String? = null
    private var newAge: String? = null
    private var newName: String? = null

    val profileResult = MutableLiveData<ActionResult<ProfileData>>()
    val saveResult = MutableLiveData<ActionResult<Boolean>>()
    val editState = MutableLiveData<Boolean>()

    fun getProfileData(userId: String) {
        if (!Utils.isConnected(App.instance)) {
            isConnected.value = false
            return
        }
        viewModelScope.launch {
            isLoading.value = true
            val browseData = repo.getProfileData(userId)
            if (browseData is RawResult.Success) {
                profileResult.value =
                    ActionResult(true, R.string.fetch_success, browseData.data)
            } else if (browseData is RawResult.Error) {
                profileResult.value = ActionResult(false, R.string.fetch_failed, null)
            }
            isLoading.value = false
        }
    }

    fun fieldDataChanged(
        newName: String? = null,
        newAge: String? = null,
        newCountry: String? = null,
        newCity: String? = null,
        newGender: String? = null,
        newAbout: String? = null,
        newImgList: MutableList<String>? = null
    ) {
        when {
            newName != null -> {
                this.newName = newName
                nameChanged = hasValueChanged(previousName, newName)
            }
            newAge != null -> {
                this.newAge = newAge
                ageChanged = hasValueChanged(previousAge, newAge)
            }
            newCountry != null -> {
                this.newCountry = newCountry
                countryChanged = hasValueChanged(previousCountry, newCountry)
            }
            newCity != null -> {
                this.newCity = newCity
                cityChanged = hasValueChanged(previousCity, newCity)
            }
            newGender != null -> {
                this.newGender = newGender
                genderChanged = hasValueChanged(previousGender, newGender)
            }
            newAbout != null -> {
                this.newAbout = newAbout
                aboutChanged = hasValueChanged(previousAbout, newAbout)
            }
            newImgList != null -> {
                this.newImgList = newImgList
                imgChanged = hasValueChanged(previousImgList, newImgList)
            }
        }

        editState.value = valueChanged()
    }

    private fun hasValueChanged(
        previousValue: MutableList<String>,
        newValue: MutableList<String>?
    ): Boolean {
        if (newValue!!.isEmpty()) {
            return false
        } else if (previousValue.size != newValue.size) {
            return true
        } else {
            for (index in previousValue.indices) {
                if (previousValue[index] != newValue[index]) {
                    return true
                }
            }
            return false
        }
    }

    private fun hasValueChanged(previousValue: String, newValue: String?): Boolean {
        if (newValue == null || newValue.isBlank()) {
            return false
        }
        return previousValue != newValue
    }

    private fun valueChanged() = nameChanged || ageChanged || countryChanged || cityChanged ||
            genderChanged || aboutChanged || imgChanged

    fun saveEditedData() {
        if (!Utils.isConnected(App.instance)) {
            isConnected.value = false
            return
        }
        viewModelScope.launch {
            isLoading.value = true
            val result = repo.saveEditedData(
                newName, newAge, newCountry, newCity, newGender,
                newAbout
            )
            if (result is RawResult.Success) {
                saveResult.value =
                    ActionResult(true, R.string.edit_saved, result.data)
            } else if (result is RawResult.Error) {
                profileResult.value = ActionResult(false, R.string.edit_failed, null)
            }
            resetSate()
            isLoading.value = false
        }
    }

    private fun resetSate() {
        nameChanged = false
        ageChanged = false
        countryChanged = false
        cityChanged = false
        genderChanged = false
        aboutChanged = false
        editState.value = false
    }
}