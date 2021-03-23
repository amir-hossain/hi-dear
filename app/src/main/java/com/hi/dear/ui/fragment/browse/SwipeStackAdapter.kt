package com.hi.dear.ui.fragment.browse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.CardItemBinding


class SwipeStackAdapter(private val mData: List<UserCore>, private val context: Context) :
    BaseAdapter() {
    private lateinit var binding: CardItemBinding

    override fun getCount(): Int {
        return mData.size
    }

    override fun getItem(position: Int): UserCore {
        return mData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var rowView: View? = convertView
        if (rowView == null) {
            binding = CardItemBinding.inflate(LayoutInflater.from(context), parent, false)
            rowView = binding.root
        }
        return rowView
    }
}