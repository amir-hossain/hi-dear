package com.hi.dear.ui

import android.app.Activity
import android.content.Context

import android.content.SharedPreferences

class PrefsManager private constructor() {

    companion object {
        const val UserId = "id"
        const val IS_LOGGED_IN = "logged_in"
        private val sharePref = PrefsManager()
        private lateinit var sharedPreferences: SharedPreferences
        fun getInstance(context: Context?): PrefsManager {
            if (!Companion::sharedPreferences.isInitialized) {
                synchronized(PrefsManager::class.java) {
                    if (!Companion::sharedPreferences.isInitialized && context != null) {
                        sharedPreferences = context!!.applicationContext!!.getSharedPreferences(
                                context.packageName,
                                Activity.MODE_PRIVATE
                            )
                        }
                    }
                }
                return sharePref
            }
        }

    fun writeFloat(key:String,value: Float) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun readFloat(key: String): Float {
        return sharedPreferences.getFloat(key, 0f)
    }

    fun writeInt(key: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun writeLong(key: String, value: Long) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun readInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun readLong(key: String): Long {
        return sharedPreferences.getLong(key, 0L)
    }

    fun writeString(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun readString(key: String): String? {
        return sharedPreferences?.getString(key, "")
    }

    fun writeBoolean(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun readBoolean(key:String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
}