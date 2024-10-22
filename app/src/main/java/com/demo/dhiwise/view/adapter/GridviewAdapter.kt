package com.demo.dhiwise.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.demo.dhiwise.model.Interest
import com.demo.dhiwise.R

class GridviewAdapter(
    private val context: Context,
    private val interests: List<Interest>
) : BaseAdapter() {
    private val selectedItems = mutableSetOf<Int>()

    override fun getCount(): Int = interests.size

    override fun getItem(position: Int): Any = interests[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.fragment3_grid_item, parent, false)
        val interest = interests[position]

        val iconImageView: ImageView = view.findViewById(R.id.icon_intrest)
        val titleTextView: TextView = view.findViewById(R.id.title_intrest)

        iconImageView.setImageResource(if (selectedItems.contains(position)) interest.selectedImageResId else interest.imageResId)
        titleTextView.text = interest.text
        titleTextView.setTextColor(if (selectedItems.contains(position)) Color.WHITE else Color.BLACK)
        view.setBackgroundResource(if (selectedItems.contains(position)) R.drawable.grid_profile_selected else R.drawable.grid_profile)

        view.setOnClickListener {
            if (selectedItems.contains(position)) {
                selectedItems.remove(position)
            } else {
                selectedItems.add(position)
            }
            notifyDataSetChanged()
        }

        return view
    }

    fun getSelectedInterests(): List<String> {
        return selectedItems.map { interests[it].text }
    }
}
