package com.hi.dear.ui

import android.app.Activity
import android.content.SharedPreferences

class PrefsManager private constructor() {

    companion object {
        const val Pic = "pic"
        const val emailOrMobile = "emailOrMobile"
        const val userName = "userName"
        const val Gender = "gender"
        const val UserId = "id"
        const val IS_LOGGED_IN = "logged_in"
        private val sharePref = PrefsManager()
        private lateinit var sharedPreferences: SharedPreferences

        fun getInstance(): PrefsManager {
            if (!Companion::sharedPreferences.isInitialized) {
                synchronized(PrefsManager::class.java) {
                    if (!Companion::sharedPreferences.isInitialized) {
                        sharedPreferences = App.instance.getSharedPreferences(
                            "com.hi.dear",
                            Activity.MODE_PRIVATE
                        )
                    }
                }
            }
            return sharePref
        }
    }

    fun writeFloat(key: String, value: Float) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor
            .putFloat(key, value)
            .apply()
    }

    fun readFloat(key: String): Float {
        return sharedPreferences.getFloat(key, 0f)
    }

    fun writeInt(key: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor
            .putInt(key, value)
            .apply()
    }

    fun writeLong(key: String, value: Long) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor
            .putLong(key, value)
            .apply()
    }

    fun readInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun readLong(key: String): Long {
        return sharedPreferences.getLong(key, 0L)
    }

    fun writeString(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor
            .putString(key, value)
            .apply()
    }

    fun readString(key: String): String? {
        return sharedPreferences?.getString(key, "")
    }

    fun writeBoolean(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor
            .putBoolean(key, value)
            .apply()
    }

    fun readBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun clearData() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor
            .clear()
            .commit()

    }
}