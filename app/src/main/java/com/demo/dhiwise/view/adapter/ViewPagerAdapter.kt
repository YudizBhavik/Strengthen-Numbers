package com.demo.dhiwise.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.demo.dhiwise.R

class ViewPagerAdapter(
    private val context: Context,
    private val imagelist: List<Int>
) : PagerAdapter() {

    override fun getCount(): Int {
        return imagelist.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val itemView: View = inflater.inflate(R.layout.layout_onboarding_view, container, false)

        val imageView: ImageView = itemView.findViewById(R.id.img_onboarding_1)
        imageView.setImageResource(imagelist[position])

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}
