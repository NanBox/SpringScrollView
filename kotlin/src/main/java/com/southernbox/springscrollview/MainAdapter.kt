package com.southernbox.springscrollview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_list.view.*


/**
 * Created by SouthernBox on 2017/6/19 0019.
 * 主页列表适配器
 */

internal class MainAdapter(private val mContext: Context,
                           private val mList: List<String>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        viewHolder.tvName.text = mList[position]
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private inner class ViewHolder internal constructor(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        internal var tvName: TextView = itemView.tv_name
    }
}