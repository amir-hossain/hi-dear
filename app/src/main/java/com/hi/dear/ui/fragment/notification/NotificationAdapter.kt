package com.hi.dear.ui.fragment.notification

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.databinding.NotificationItemBinding
import com.hi.dear.ui.Constant
import com.hi.dear.ui.base.BaseViewHolder

class NotificationAdapter(private val listener: IMessageClickListener?) :
    ListAdapter<NotificationData, NotificationAdapter.NotificationViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<NotificationData>() {
        override fun areItemsTheSame(
            oldItem: NotificationData,
            newItem: NotificationData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NotificationData,
            newItem: NotificationData
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val data = getItem(position)
        Glide.with(holder.itemView.context)
            .load(data.picture)
            .into(holder.binding.image)
        val text = getText(holder, data)
        holder.binding.msg.text = getHtmlFormatted(text)
        holder.binding.date.text = data.time
        if (data.notificationType == Constant.notification_type_request_accepted) {
            holder.binding.specialText.text = "Chat now"
            holder.binding.specialText.visibility = View.VISIBLE
        } else {
            holder.binding.specialText.visibility = View.GONE
        }
    }

    private fun getHtmlFormatted(text: String): CharSequence? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml(text)
        }
    }

    private fun getText(holder: NotificationViewHolder, data: NotificationData): String {
        var msg = ""
        if (data.notificationType == Constant.notification_type_request_accepted) {
            msg = " accepted your request"
        }
        return holder.itemView.context.getString(R.string.notification_text, data.name, msg)
    }


    inner class NotificationViewHolder : BaseViewHolder {
        var binding: NotificationItemBinding

        constructor(view: View) : super(view) {
            binding = NotificationItemBinding.bind(view)
            view.setOnClickListener {
                listener?.onItemClick(getItem(adapterPosition))
            }
        }
    }

    interface IMessageClickListener {
        fun onItemClick(data: NotificationData)
    }
}