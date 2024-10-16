package com.demo.dhiwise.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.demo.dhiwise.LoginScreen
import com.demo.dhiwise.R


class ViewPagerAdapter(
    private val context: Context,
    private val imagelist: List<Int>,
    private val txtlist_title: List<Int>,
    private val txtlist_desc: List<Int>,
    private val viewPager: ViewPager
) : PagerAdapter() {

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
        val imageView: ImageView = itemView.findViewById(R.id.img_onboarding_1)
        val textView_title: TextView = itemView.findViewById(R.id.txt_onboarding_title)
        val textView_desc: TextView = itemView.findViewById(R.id.txt_onboarding_desc)
        val skipButton: TextView = itemView.findViewById(R.id.txt_btn_skip)
        val nextButton: Button = itemView.findViewById(R.id.btn_next)

        textView_title.setText(txtlist_title[position])
        textView_desc.setText(txtlist_desc[position])
        imageView.setImageResource(imagelist[position])

        skipButton.setOnClickListener {
            viewPager.setCurrentItem(imagelist.size - 1, true)
        }

        nextButton.setOnClickListener {
            if (position < imagelist.size - 1) {
                viewPager.setCurrentItem(position + 1, true)
            } else {
                val intent = Intent(context, LoginScreen::class.java)
                context.startActivity(intent)
            }
        }

        updateButtonVisibility(nextButton, skipButton, position)
        container.addView(itemView)
        return itemView
    }

    private fun updateButtonVisibility(nextButton: Button, skipButton: TextView, position: Int) {
        skipButton.visibility = if (position == imagelist.size - 1) View.GONE else View.VISIBLE
        nextButton.text = if (position == imagelist.size - 1) "Get Started" else "Next"
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}
