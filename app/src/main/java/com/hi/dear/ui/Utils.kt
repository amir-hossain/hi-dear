package com.hi.dear.ui

import android.view.View
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object Utils {

    fun disableView(view: View) {
        view.isEnabled = false
        view.alpha = .3f
    }

    fun enableView(view: View) {
        view.isEnabled = true
        view.alpha = 1f
    }

    fun getHash(s: String): String {
        try {

            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuffer()
            for (i in messageDigest.indices) hexString.append(
                Integer.toHexString(
                    0xFF and messageDigest[i]
                        .toInt()
                )
            )
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return s
    }
}