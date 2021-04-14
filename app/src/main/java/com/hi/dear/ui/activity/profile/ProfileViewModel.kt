package com.hi.dear.ui.activity.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hi.dear.R
import com.hi.dear.data.RawResult
import com.hi.dear.data.model.common.ProfileData
import com.hi.dear.repo.ProfileRepository
import com.hi.dear.ui.activity.ActionResult
import com.hi.dear.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: ProfileRepository) : BaseViewModel() {

    private var nameChanged = false
    private var ageChanged = false
    private var countryChanged = false
    private var cityChanged = false
    private var genderChanged = false
    private var aboutChanged = false

    var previousAbout = ""
    var previousGender = ""
    var previousCity = ""
    var previousCountry = ""
    var previousAge = ""
    var previousName = ""

    var newAbout: String? = null
    var newGender: String? = null
    var newCity: String? = null
    var newCountry: String? = null
    var newAge: String? = null
    var newName: String? = null

    val profileResult = MutableLiveData<ActionResult<ProfileData>>()
    val saveResult = MutableLiveData<ActionResult<Boolean>>()
    val editState = MutableLiveData<Boolean>()

    fun getProfileData(userId: String) {
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
        newName: String? = null, newAge: String? = null, newCountry: String? = null,
        newCity: String? = null, newGender: String? = null, newAbout: String? = null
    ) {
        this.newName = newName
        this.newAge = newAge
        this.newCountry = newCountry
        this.newCity = newCity
        this.newGender = newGender
        this.newAbout = newAbout

        when {
            newName != null -> {
                nameChanged = hasValueChanged(previousName, newName)
            }
            newAge != null -> {
                ageChanged = hasValueChanged(previousAge, newAge)
            }
            newCountry != null -> {
                countryChanged = hasValueChanged(previousCountry, newCountry)
            }
            newCity != null -> {
                cityChanged = hasValueChanged(previousCity, newCity)
            }
            newGender != null -> {
                genderChanged = hasValueChanged(previousGender, newGender)
            }
            newAbout != null -> {
                aboutChanged = hasValueChanged(previousAbout, newAbout)
            }
        }

        editState.value = valueChanged()
    }

    private fun hasValueChanged(previousValue: String, newValue: String?): Boolean {
        if (newValue == null || newValue.isBlank()) {
            return false
        }
        return previousValue != newValue
    }

    private fun valueChanged() = nameChanged || ageChanged || countryChanged || cityChanged ||
            genderChanged || aboutChanged

    fun saveEditedData() {
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
    }
}