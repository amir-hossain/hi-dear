package com.hi.dear.ui.activity.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.databinding.MessageItemBinding
import com.hi.dear.ui.base.BaseViewHolder

class MessageAdapter(private val listener: IMessageClickListener?) :
    ListAdapter<MessageData, MessageAdapter.MessageViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<MessageData>() {
        override fun areItemsTheSame(oldItem: MessageData, newItem: MessageData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MessageData, newItem: MessageData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val data = getItem(position)
        Glide.with(holder.itemView.context)
            .load(data.picture)
            .into(holder.binding.image)

        if (data.online_visibility) {
            holder.binding.onlineStatus.visibility = View.VISIBLE
        } else {
            holder.binding.onlineStatus.visibility = View.GONE
        }

        if (data.isFavourite) {
            holder.binding.favouriteStatus.visibility = View.VISIBLE
        } else {
            holder.binding.favouriteStatus.visibility = View.GONE
        }

        holder.binding.name.text = data.name
        holder.binding.message.text = data.message
        if (data.messageCount != null && data.messageCount != 0) {
            holder.binding.newMessage.text = data.messageCount.toString()
            holder.binding.newMessage.visibility = View.VISIBLE
        } else {
            holder.binding.newMessage.visibility = View.GONE
        }
        holder.binding.date.text = data.time
    }

    inner class MessageViewHolder : BaseViewHolder {
        var binding: MessageItemBinding

        constructor(view: View) : super(view) {
            binding = MessageItemBinding.bind(view)
            view.setOnClickListener {
                listener?.onItemClick(getItem(adapterPosition))
            }
        }
    }

    interface IMessageClickListener {
        fun onItemClick(data: MessageData)
    }
}