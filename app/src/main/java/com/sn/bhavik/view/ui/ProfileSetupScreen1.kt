package com.sn.bhavik.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.sn.bhavik.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class ProfileSetupScreen1 : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var stepProgressBar: LinearLayout
    private lateinit var nextButton: MaterialButton
    private lateinit var profileTitle: TextView
    private lateinit var profileDesc: TextView
    private lateinit var txtBtnPrevious: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_setup_screen1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupViewPager()
        updateStepProgress(1)

        nextButton.setOnClickListener {
            handleNextButtonClick()
        }

        txtBtnPrevious.setOnClickListener {
            handlePreviousButtonClick()
        }
    }

    private fun initViews() {
        nextButton = findViewById(R.id.btn_next_profile_1)
        profileTitle = findViewById(R.id.txt_profile_title)
        profileDesc = findViewById(R.id.txt_profile_desc)
        txtBtnPrevious = findViewById(R.id.txt_btn_previous)
        viewPager = findViewById(R.id.viewPager)
        stepProgressBar = findViewById(R.id.stepProgressBar)
    }

    private fun setupViewPager() {
        viewPager.adapter = ProfileSetupAdapter(this)
        viewPager.isUserInputEnabled = false
    }

    @SuppressLint("SetTextI18n")
    private fun updateStepProgress(currentStep: Int) {
        for (i in 0 until stepProgressBar.childCount) {
            val view = stepProgressBar.getChildAt(i)
            view.setBackgroundColor(if (i < currentStep) {
                ContextCompat.getColor(this, R.color.p60)
            } else {
                ContextCompat.getColor(this, R.color.S90)
            })
        }
        txtBtnPrevious.visibility = if (currentStep == 1) View.INVISIBLE else View.VISIBLE
        nextButton.text = if (currentStep == 3) "Submit" else "Next"
                profileDesc.text = if (currentStep == 3) "Choose minimum 3 interests." else "Please complete the fields below to set up your profile."
    }

    private fun handleNextButtonClick() {
        val currentItem = viewPager.currentItem
        val canProceed = when (currentItem) {
            0 -> (supportFragmentManager.fragments[0] as FragmentProfileSetup1).updateProfile()
            1 -> (supportFragmentManager.fragments[1] as FragmentProfileSetup2).updateProfile()
            2 -> (supportFragmentManager.fragments[2] as FragmentProfileSetup3).updateProfile()
            else -> false
        }


        if (canProceed) {
            viewPager.setCurrentItem(currentItem + 1, true)
            onFragmentChanged(viewPager.currentItem)
        }
    }

    private fun handlePreviousButtonClick() {
        val currentItem = viewPager.currentItem
        if (currentItem > 0) {
            viewPager.setCurrentItem(currentItem - 1, true)
        }
        onFragmentChanged(viewPager.currentItem)
    }

    fun onFragmentChanged(position: Int) {
        updateStepProgress(position + 1)
    }

//    private fun showProgressBar(show: Boolean) {
//        nextButton.isEnabled = !show
//        nextButton.text = if (show) "" else getString(R.string.btn_txt_next)
//    }

    private inner class ProfileSetupAdapter(fragmentActivity: AppCompatActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> FragmentProfileSetup1().apply {
                    onProfileUpdateSuccess = { onFragmentChanged(viewPager.currentItem) }
                }
                1 -> FragmentProfileSetup2().apply {
                    onProfileUpdateSuccess = { onFragmentChanged(viewPager.currentItem) }
                }
                else -> FragmentProfileSetup3()
            }
        }
    }
}
