package com.hi.dear.ui.fragment.message

import android.view.View
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.databinding.MessageItemBinding
import com.hi.dear.ui.base.BaseAdapter
import com.hi.dear.ui.base.BaseViewHolder

class MessageAdapter(private val listener: IMessageClickListener?) : BaseAdapter<MessageData>() {
    override fun setViewId(viewType: Int): Int {
        return R.layout.message_item
    }

    override fun bindView(holder: BaseViewHolder, data: MessageData) {
        if (holder is MessageViewHolder) {
            Glide.with(context)
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
    }

    override fun setViewHolder(view: View, viewType: Int): BaseViewHolder {
        return MessageViewHolder(view)
    }

    inner class MessageViewHolder : BaseViewHolder {
        var binding: MessageItemBinding

        constructor(view: View) : super(view) {
            binding = MessageItemBinding.bind(view)
            view.setOnClickListener {
                listener?.onItemClick(dataList[adapterPosition])
            }
        }
    }

    interface IMessageClickListener {
        fun onItemClick(data: MessageData)
    }
}