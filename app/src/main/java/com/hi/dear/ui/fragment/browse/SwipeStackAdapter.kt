package com.hi.dear.ui.fragment.browse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.data.model.common.UserCore
import com.hi.dear.databinding.CardItemBinding


class SwipeStackAdapter : RecyclerView.Adapter<SwipeStackAdapter.ViewHolder>() {
    private val mData = mutableListOf<UserCore>()

    fun addItem(userList: MutableList<UserCore>) {
        mData.addAll(userList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mData[position].picture)
            .into(holder.binding.textViewCard)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardItemBinding = CardItemBinding.bind(view)
    }
}