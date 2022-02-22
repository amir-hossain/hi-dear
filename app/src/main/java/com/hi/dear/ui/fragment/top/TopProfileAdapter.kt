package com.hi.dear.ui.fragment.top

import android.view.View
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.databinding.TopProfileItemBinding
import com.hi.dear.ui.Constant
import com.hi.dear.ui.base.BaseAdapter
import com.hi.dear.ui.base.BaseViewHolder

class TopProfileAdapter(private val listener: ITopProfileClickListener?) :
    BaseAdapter<TopProfileData>() {
    private var holder: TopProfileViewHolder? = null

    override fun setViewId(viewType: Int): Int {
        return R.layout.request_item
    }

    override fun bindView(holder: BaseViewHolder, data: TopProfileData) {
        if (holder is TopProfileViewHolder) {
            Glide.with(context)
                .load(data.picture)
                .into(holder.binding.image)

            when (data.status) {
                Constant.requestNew -> {
                    holder.binding.declinedMsg.visibility = View.GONE
                    holder.binding.chatBtn.visibility = View.GONE

                }
                Constant.requestAccepted -> {
                    holder.binding.declinedMsg.visibility = View.GONE
                    holder.binding.chatBtn.visibility = View.VISIBLE
                }
                else -> {
                    holder.binding.declinedMsg.visibility = View.VISIBLE
                    holder.binding.chatBtn.visibility = View.GONE
                }
            }

            holder.binding.name.text = data.name
        }
    }

    override fun setViewHolder(view: View, viewType: Int): BaseViewHolder {
        holder = TopProfileViewHolder(view)
        return holder as TopProfileViewHolder
    }

    fun updateView(data: TopProfileData) {
        var dataPosition = -1
        for (i in dataList.indices) {
            if (dataList[i].id == data.id) {
                dataList[i].status = data.status
                dataPosition = i
            }
        }

        if (dataPosition != -1) {
            notifyItemChanged(dataPosition)
        }

    }

    inner class TopProfileViewHolder(view: View) : BaseViewHolder(view) {
        var binding: TopProfileItemBinding = TopProfileItemBinding.bind(view)

        init {
            binding.btnClose.setOnClickListener {
                var itemPosition = getPosition(view)
                var removeData = dataList.removeAt(itemPosition)
                listener?.onCloseClick(removeData)
                notifyDataSetChanged()
            }
            binding.chatBtn.setOnClickListener {
                var data = dataList[getPosition(view)]
                listener?.onChatClick(data)
            }
            binding.image.setOnClickListener {
                var data = dataList[getPosition(view)]
                listener?.onImageClick(data)
            }
        }
    }


    interface ITopProfileClickListener {
        fun onCloseClick(data: TopProfileData)
        fun onChatClick(data: TopProfileData)
        fun onImageClick(data: TopProfileData)
    }
}