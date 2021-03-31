package com.hi.dear.ui.activity.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.data.model.common.Chat
import com.hi.dear.databinding.ItemChatLeftBinding
import com.hi.dear.databinding.ItemChatRightBinding
import com.hi.dear.ui.PrefsManager
import com.hi.dear.ui.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter : ListAdapter<Chat, BaseViewHolder>(GridViewDiffCallback) {
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
        val senderId = getItem(position)!!.senderId!!
        val mineId = PrefsManager.getInstance().readString(PrefsManager.UserId)!!
        return if (senderId == mineId) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        var view: View
        return if (viewType == R.layout.item_chat_left) {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_left, parent, false)
            LeftMsgViewHolder(view)
        } else {
            view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_right, parent, false)
            RightMsgViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data = getItem(position)
        if (holder is LeftMsgViewHolder) {
            holder.binding.message.text = data.text
            Glide
                .with(holder.itemView.context)
                .load(data.photoUrl)
                .into(holder.binding.image)
            holder.binding.time.text = df.format(Date())
        } else if (holder is RightMsgViewHolder) {
            holder.binding.message.text = data.text
            holder.binding.time.text = df.format(Date())
        }
    }
}