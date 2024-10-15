package com.demo.dhiwise.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.demo.dhiwise.R
import java.util.Objects

class ViewPagerAdapter(val context: Context,val imagelist: List<Int>,val txtlist_title: List<Int>,val txtlist_desc: List<Int>): PagerAdapter() {
    override fun getCount(): Int {
       return imagelist.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View = mLayoutInflater.inflate(R.layout.layout_onboarding_view, container, false)
        val imageView: ImageView = itemView.findViewById<View>(R.id.img_onboarding_1) as ImageView
        val textView_title: TextView = itemView.findViewById<TextView>(R.id.txt_onboarding_title) as TextView
        val textView_desc: TextView = itemView.findViewById<TextView>(R.id.txt_onboarding_desc) as TextView

        textView_title.setText(txtlist_title.get(position))
        textView_desc.setText(txtlist_desc.get(position))
        imageView.setImageResource(imagelist.get(position))
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}