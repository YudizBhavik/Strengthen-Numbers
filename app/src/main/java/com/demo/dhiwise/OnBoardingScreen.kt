package com.demo.dhiwise

import android.os.Bundle
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import android.widget.TextView
import me.relex.circleindicator.CircleIndicator
import com.demo.dhiwise.view.adapter.ViewPagerAdapter

class OnBoardingScreen : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var circleIndicator: CircleIndicator
    lateinit var imageList: List<Int>

    val titles = listOf(
        R.string.txt_title1_experience_culture,
        R.string.txt_title_2post_workout,
        R.string.txt_title_3join_fitness
    )

    val descriptions = listOf(
        R.string.txt_desc_1join_community,
        R.string.txt_desc_2motivate_inspire,
        R.string.txt_desc_3attending_group
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.activity_on_boarding_screen)

        viewPager = findViewById(R.id.onboard_viewpager)
        circleIndicator = findViewById(R.id.circle_indicator)

        imageList = listOf(
            R.drawable.onboarding_img1,
            R.drawable.onboarding_img2,
            R.drawable.onboarding_img3
        )

        viewPagerAdapter = ViewPagerAdapter(this, imageList)
        viewPager.adapter = viewPagerAdapter

        circleIndicator.setViewPager(viewPager)

        val titleTextView: TextView = findViewById(R.id.txt_onboarding_title)
        val descTextView: TextView = findViewById(R.id.txt_onboarding_desc)
        val skipButton: TextView = findViewById(R.id.txt_btn_skip)
        val nextButton: MaterialButton = findViewById(R.id.btn_next)

        titleTextView.setText(titles[0])
        descTextView.setText(descriptions[0])

        skipButton.setOnClickListener {
            viewPager.setCurrentItem(imageList.size - 1, true)
        }

        nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < imageList.size - 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
            } else {
                startActivity(Intent(this, LoginScreen::class.java))
                finish()
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                titleTextView.setText(titles[position])
                descTextView.setText(descriptions[position])

                if (position == imageList.size - 1) {
                    skipButton.visibility = TextView.GONE
                    nextButton.text = "Get Started"
                } else {
                    skipButton.visibility = TextView.VISIBLE
                    nextButton.text = "Next"
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}
