package com.demo.dhiwise.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.demo.dhiwise.GridItem
import com.demo.dhiwise.R

class GridviewAdapter(
    private val context: Context,
    private val dataList: List<GridItem>
) : BaseAdapter() {

    override fun getCount(): Int = dataList.size

    override fun getItem(position: Int): GridItem = dataList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.fragment3_grid_item, parent, false)

        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)

        val dataItem = getItem(position)
        imageView.setImageResource(dataItem.imageResId)
        textView.text = dataItem.text

        return itemView
    }
}
