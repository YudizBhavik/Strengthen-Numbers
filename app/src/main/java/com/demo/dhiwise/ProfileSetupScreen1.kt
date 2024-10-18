package com.demo.dhiwise

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileSetupScreen1 : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_setup_screen1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ProfileSetupAdapter(this)
        viewPager.isUserInputEnabled = false

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.isSmoothScrollingEnabled = false
        tabLayout.isClickable = false
        TabLayoutMediator(tabLayout, viewPager) { tab, position -> }.attach()
    }

    private inner class ProfileSetupAdapter(fragmentActivity: AppCompatActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 3
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FragmentProfileSetup1()
                1 -> FragmentProfileSetup2()
                else -> FragmentProfileSetup3()
            }
        }
    }
}
