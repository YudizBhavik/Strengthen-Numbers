package com.demo.dhiwise

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager
import com.demo.dhiwise.view.adapter.ViewPagerAdapter

class OnBoardingScreen : AppCompatActivity() {

    lateinit var viewPager2: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter
    lateinit var imageList: List<Int>
    lateinit var txtlist_title: List<Int>
    lateinit var txtlist_desc: List<Int>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.activity_on_boarding_screen)

        viewPager2 = findViewById(R.id.onboard_viewpager)
        imageList = ArrayList<Int>()
        imageList = imageList + R.drawable.onboarding_img1
        imageList = imageList + R.drawable.onboarding_img2
        imageList = imageList + R.drawable.onboarding_img3

        txtlist_title = ArrayList<Int>()
        txtlist_title = txtlist_title + R.string.txt_title1_experience_culture
        txtlist_title = txtlist_title + R.string.txt_title_2post_workout
        txtlist_title = txtlist_title + R.string.txt_title_3join_fitness

        txtlist_desc = ArrayList<Int>()
        txtlist_desc = txtlist_desc + R.string.txt_desc_1join_community
        txtlist_desc = txtlist_desc + R.string.txt_desc_2motivate_inspire
        txtlist_desc = txtlist_desc + R.string.txt_desc_3attending_group

        viewPagerAdapter = ViewPagerAdapter(this@OnBoardingScreen, imageList, txtlist_title, txtlist_desc, viewPager2,)
        viewPager2.adapter = viewPagerAdapter
    }
}