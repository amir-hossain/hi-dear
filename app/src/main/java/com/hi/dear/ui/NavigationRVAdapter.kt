package com.hi.dear.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hi.dear.R

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
                    R.color.transparent_primary
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

        holder.navigationTitle.text = items[position].title

        holder.navigationIcon.setImageResource(items[position].icon)

    }

    fun highlight(position: Int) {
        currentPos = position
        notifyDataSetChanged()
    }

    inner class NavigationItemViewHolder : RecyclerView.ViewHolder {
        var navigationIcon: ImageView
        var navigationTitle: TextView

        constructor(itemView: View) : super(itemView) {
            navigationIcon = itemView.findViewById(R.id.navigation_icon)
            navigationTitle = itemView.findViewById(R.id.navigation_title)
            itemView.setOnClickListener {
                clickListener?.onClick(itemView, adapterPosition)
            }
        }
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)
    }
}