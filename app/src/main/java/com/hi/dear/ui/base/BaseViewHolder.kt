package com.hi.dear.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    /*position is need where adapter position is now working like grid layout*/
    fun embed(position: Int) {
        view.tag = position
    }

    fun getPosition(view: View?): Int {
        return view?.tag as Int
    }
}