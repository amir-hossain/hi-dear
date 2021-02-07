package com.hi.dear.ui

import android.text.method.PasswordTransformationMethod
import android.view.View

class PasswordTransformation : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence?, view: View?): CharSequence? {
        return PasswordCharSequence(source ?: "")
    }

    private class PasswordCharSequence(private val source: CharSequence) : CharSequence {
        override val length: Int
            get() = source.length

        override fun get(index: Int): Char {
            return '.'
        }

        override fun subSequence(start: Int, end: Int): CharSequence {
            return source.subSequence(start, end) // Return default
        }
    }
}