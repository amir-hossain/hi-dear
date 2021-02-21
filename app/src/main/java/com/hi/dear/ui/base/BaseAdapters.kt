package com.hi.dear.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapters<Data> :
    RecyclerView.Adapter<BaseViewHolder>() {
    protected lateinit var context: Context
    protected var dataList: MutableList<Data> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(setViewId(viewType), parent, false)
        return setViewHolder(view, viewType)
    }

    abstract fun setViewId(viewType: Int): Int

    abstract fun bindView(holder: BaseViewHolder, data: Data)

    abstract fun setViewHolder(view: View, viewType: Int): BaseViewHolder

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.embed(position)
        bindView(holder, dataList[position])
    }

    open fun addData(dataList: MutableList<Data>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}