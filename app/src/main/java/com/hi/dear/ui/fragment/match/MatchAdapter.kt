package com.hi.dear.ui.fragment.match

import android.view.View
import com.bumptech.glide.Glide
import com.hi.dear.R
import com.hi.dear.databinding.MatchItemBinding
import com.hi.dear.ui.base.BaseAdapters
import com.hi.dear.ui.base.BaseViewHolder
import java.text.SimpleDateFormat
import java.util.*

class MatchAdapter(private val listener: IMatchClickListener?) : BaseAdapters<MatchData>() {
    override fun setViewId(viewType: Int): Int {
        return R.layout.match_item
    }

    override fun bindView(holder: BaseViewHolder, data: MatchData) {
        if (holder is MatchViewHolder) {
            Glide.with(context)
                .load(data.image)
                .into(holder.binding.image)

            if (data.message != null) {
                holder.binding.btnGroup.visibility = View.GONE
                holder.binding.message.visibility = View.VISIBLE
                holder.binding.message.text = data.message
            } else {
                holder.binding.btnGroup.visibility = View.VISIBLE
                holder.binding.message.visibility = View.GONE
            }

            holder.binding.name.text = data.name
        }
    }

    override fun setViewHolder(view: View, viewType: Int): BaseViewHolder {
        return MatchViewHolder(view)
    }

    inner class MatchViewHolder : BaseViewHolder {
        var binding: MatchItemBinding

        constructor(view: View) : super(view) {
            binding = MatchItemBinding.bind(view)

            binding.btnClose.setOnClickListener {
                var itemPosition = getPosition(view)
                var removeData = dataList.removeAt(itemPosition)
                listener?.onCloseClick(removeData)
                notifyDataSetChanged()
            }
            binding.btnAccept.setOnClickListener {
                var data = dataList[getPosition(view)]
                var itemPosition = getPosition(view)
                var msg = context.getString(R.string.accept_msg, getTodayDate())
                data.message = msg
                notifyItemChanged(itemPosition)
                listener?.onAcceptClick(data)
            }
            binding.btnNo.setOnClickListener {
                var data = dataList[getPosition(view)]
                var itemPosition = getPosition(view)
                var msg = context.getString(R.string.rejected)
                data.message = msg
                notifyItemChanged(itemPosition)
                listener?.onNoClick(data)
            }
        }
    }

    private fun getTodayDate(): String {
        var formate = SimpleDateFormat("d MMM")
        return formate.format(Date())
    }

    interface IMatchClickListener {
        fun onCloseClick(data: MatchData)
        fun onAcceptClick(data: MatchData)
        fun onNoClick(data: MatchData)
    }
}