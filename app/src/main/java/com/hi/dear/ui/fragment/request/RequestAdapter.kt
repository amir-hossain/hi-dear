package com.hi.dear.ui.fragment.request

import android.view.View
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.databinding.RequestItemBinding
import com.hi.dear.ui.Constant
import com.hi.dear.ui.base.BaseAdapter
import com.hi.dear.ui.base.BaseViewHolder

class RequestAdapter(private val listener: IRequestClickListener?) : BaseAdapter<RequestData>() {
    override fun setViewId(viewType: Int): Int {
        return R.layout.request_item
    }

    override fun bindView(holder: BaseViewHolder, data: RequestData) {
        if (holder is RequestViewHolder) {
            Glide.with(context)
                .load(data.picture)
                .into(holder.binding.image)

            when (data.status) {
                Constant.requestNew -> {
                    holder.binding.btnGroup.visibility = View.VISIBLE
                    holder.binding.declinedMsg.visibility = View.GONE
                    holder.binding.chatBtn.visibility = View.GONE

                }
                Constant.requestAccepted -> {
                    holder.binding.btnGroup.visibility = View.GONE
                    holder.binding.declinedMsg.visibility = View.GONE
                    holder.binding.chatBtn.visibility = View.VISIBLE
                }
                else -> {
                    holder.binding.btnGroup.visibility = View.GONE
                    holder.binding.declinedMsg.visibility = View.VISIBLE
                    holder.binding.chatBtn.visibility = View.GONE
                }
            }

            holder.binding.name.text = data.name
        }
    }

    override fun setViewHolder(view: View, viewType: Int): BaseViewHolder {
        return RequestViewHolder(view)
    }

    fun updateView(data: RequestData) {
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


    inner class RequestViewHolder : BaseViewHolder {
        var binding: RequestItemBinding

        constructor(view: View) : super(view) {
            binding = RequestItemBinding.bind(view)

            binding.btnClose.setOnClickListener {
                var itemPosition = getPosition(view)
                var removeData = dataList.removeAt(itemPosition)
                listener?.onCloseClick(removeData)
                notifyDataSetChanged()
            }
            binding.btnAccept.setOnClickListener {
                var data = dataList[getPosition(view)]
                listener?.onAcceptClick(data)
            }
            binding.btnNo.setOnClickListener {
                var data = dataList[getPosition(view)]
                listener?.onNoClick(data)
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


    interface IRequestClickListener {
        fun onCloseClick(data: RequestData)
        fun onAcceptClick(data: RequestData)
        fun onNoClick(data: RequestData)
        fun onChatClick(data: RequestData)
        fun onImageClick(data: RequestData)
    }
}