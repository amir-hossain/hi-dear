package com.hi.dear.ui.activity.chat

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.data.model.common.Chat
import com.hi.dear.databinding.ItemChatLeftBinding
import com.hi.dear.databinding.ItemChatRightBinding
import com.hi.dear.ui.base.BaseAdapter
import com.hi.dear.ui.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class MsgAdapter() : BaseAdapter<Chat>() {
    private val df = SimpleDateFormat("HH:mm", Locale.getDefault())

    companion object GridViewDiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = dataList[position]
        return if (item.isOwner) {
            R.layout.item_chat_right
        } else {
            R.layout.item_chat_left
        }
    }

    inner class LeftMsgViewHolder : BaseViewHolder {
        var binding: ItemChatLeftBinding

        constructor(view: View) : super(view) {
            binding = ItemChatLeftBinding.bind(view)
        }
    }

    inner class RightMsgViewHolder : BaseViewHolder {
        var binding: ItemChatRightBinding

        constructor(view: View) : super(view) {
            binding = ItemChatRightBinding.bind(view)
        }
    }

    override fun setViewId(viewType: Int): Int {
        return when (viewType) {
            R.layout.item_chat_left -> R.layout.item_chat_left
            R.layout.item_chat_right -> R.layout.item_chat_right
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun bindView(holder: BaseViewHolder, data: Chat) {
        if (holder is LeftMsgViewHolder) {
            holder.binding.message.text = data.text
            Glide
                .with(context)
                .load(R.drawable.sample_profile_2)
                .into(holder.binding.image)
            holder.binding.time.text = df.format(Date())
        } else if (holder is RightMsgViewHolder) {
            holder.binding.message.text = data.text
            holder.binding.time.text = df.format(Date())
        }
    }

    override fun setViewHolder(view: View, viewType: Int): BaseViewHolder {
        return if (viewType == R.layout.item_chat_left) {
            LeftMsgViewHolder(view)
        } else {
            RightMsgViewHolder(view)
        }
    }
}