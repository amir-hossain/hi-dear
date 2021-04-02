package com.hi.dear.ui.activity.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hi.dear.R
import com.hi.dear.databinding.RowNavDrawerBinding
import com.hi.dear.ui.Constant

class NavigationRVAdapter(
    private var items: ArrayList<NavigationItemModel>,
    private val clickListener: ClickListener?
) : RecyclerView.Adapter<NavigationRVAdapter.NavigationItemViewHolder>() {

    private var currentPos = 0
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationItemViewHolder {
        context = parent.context
        val navItem =
            LayoutInflater.from(parent.context).inflate(R.layout.row_nav_drawer, parent, false)
        return NavigationItemViewHolder(navItem)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {
        // To highlight the selected Item, show different background color
        if (position == currentPos) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.color_selected
                )
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.transparent
                )
            )
        }
        val title = items[position].title
        holder.binding.navigationTitle.text = title
        if (title == Constant.tipsFragmentTitle) {
            holder.binding.tag.visibility = View.VISIBLE
        } else {
            holder.binding.tag.visibility = View.GONE
        }

        holder.binding.navigationIcon.setImageResource(items[position].icon)

    }

    fun highlight(position: Int) {
        currentPos = position
        notifyDataSetChanged()
    }

    inner class NavigationItemViewHolder : RecyclerView.ViewHolder {
        var binding: RowNavDrawerBinding

        constructor(itemView: View) : super(itemView) {
            binding = RowNavDrawerBinding.bind(itemView)
            itemView.setOnClickListener {
                clickListener?.onClick(itemView, adapterPosition)
            }
        }
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)
    }
}