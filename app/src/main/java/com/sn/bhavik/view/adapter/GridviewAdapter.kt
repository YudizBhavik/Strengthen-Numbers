package com.sn.bhavik.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.sn.bhavik.model.Interest
import com.sn.bhavik.R

class GridviewAdapter(
    private val context: Context,
    private var interests: List<Interest>
) : BaseAdapter() {

    override fun getCount(): Int = interests.size

    override fun getItem(position: Int): Any = interests[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.fragment3_grid_item, parent, false)
        val interest = interests[position]

        val iconImageView: ImageView = view.findViewById(R.id.icon_intrest)
        val titleTextView: TextView = view.findViewById(R.id.title_intrest)

        // Show the selected image if the item is selected, otherwise show the unselected image
        iconImageView.setImageResource(if (interest.isSelected) interest.selectedImageResId else interest.imageResId)
        titleTextView.text = interest.text
        titleTextView.setTextColor(if (interest.isSelected) Color.WHITE else Color.BLACK)
        view.setBackgroundResource(if (interest.isSelected) R.drawable.grid_profile_selected else R.drawable.grid_profile)

        // Handle item click to toggle selection
        view.setOnClickListener {
            interest.isSelected = !interest.isSelected
            notifyDataSetChanged()
        }

        return view
    }

    // Get a list of selected interests
    fun getSelectedInterests(): List<String> {
        return interests.filter { it.isSelected }.map { it.text }
    }

    // Update the interest list, e.g., after getting profile data
    fun updateInterests(newInterests: List<Interest>) {
        interests = newInterests
        notifyDataSetChanged()
    }
}
