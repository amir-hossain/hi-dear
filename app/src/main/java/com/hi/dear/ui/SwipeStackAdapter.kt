package com.hi.dear.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hi.dear.R


class SwipeStackAdapter(private val mData: List<String>, private val context: Context) :
    BaseAdapter() {
    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): String {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var rowView: View? = convertView
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false)
        }
        return rowView
    }
}