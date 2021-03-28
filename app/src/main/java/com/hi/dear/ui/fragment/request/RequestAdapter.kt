package com.hi.dear.ui.fragment.request

import android.view.View
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.databinding.RequestItemBinding
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

            if (data.isAdded) {
                holder.binding.btnGroup.visibility = View.GONE
                holder.binding.message.visibility = View.VISIBLE
            } else {
                holder.binding.btnGroup.visibility = View.VISIBLE
                holder.binding.message.visibility = View.GONE
            }

            holder.binding.name.text = data.name
        }
    }

    override fun setViewHolder(view: View, viewType: Int): BaseViewHolder {
        return RequestViewHolder(view)
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
                var itemPosition = getPosition(view)
                notifyItemChanged(itemPosition)
                listener?.onAcceptClick(data)
            }
            binding.btnNo.setOnClickListener {
                var data = dataList[getPosition(view)]
                var itemPosition = getPosition(view)
                var msg = context.getString(R.string.rejected)
                notifyItemChanged(itemPosition)
                listener?.onNoClick(data)
            }
        }
    }

    interface IRequestClickListener {
        fun onCloseClick(data: RequestData)
        fun onAcceptClick(data: RequestData)
        fun onNoClick(data: RequestData)
    }
}