package com.demo.dhiwise.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
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
import com.demo.dhiwise.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class ProfileSetupScreen1 : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var stepProgressBar: LinearLayout
    private lateinit var nextButton: MaterialButton
    private lateinit var profile_title: TextView
    private lateinit var profile_desc: TextView
    private lateinit var txt_btn_previous: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_setup_screen1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        profile_title = findViewById(R.id.txt_profile_title)
        profile_desc = findViewById(R.id.txt_profile_desc)
        txt_btn_previous = findViewById(R.id.txt_btn_previous)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = ProfileSetupAdapter(this)
        viewPager.isUserInputEnabled = false

        stepProgressBar = findViewById(R.id.stepProgressBar)
        updateStepProgress(1)

        nextButton = findViewById(R.id.btn_next_profile_1)
        nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < 2) {
                viewPager.setCurrentItem(currentItem + 1, true)
            }
            onFragmentChanged(viewPager.currentItem)
        }

        txt_btn_previous.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1, true)
            }
            onFragmentChanged(viewPager.currentItem)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateStepProgress(currentStep: Int) {
        for (i in 0 until stepProgressBar.childCount) {
            val view = stepProgressBar.getChildAt(i)
            if (i < currentStep) {
                view.setBackgroundColor(ContextCompat.getColor(this, R.color.p60))
            } else {
                view.setBackgroundColor(ContextCompat.getColor(this, R.color.S90))
            }
        }

        nextButton = findViewById(R.id.btn_next_profile_1)
        if (currentStep == 3) {
            nextButton.text = "Submit"
            profile_desc.text = "Choose minimum 3 interests."
        } else {
            nextButton.text = "Next"
            profile_desc.text = "Please complete the fields below to set up your profile."
        }
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

    fun onFragmentChanged(position: Int) {
        updateStepProgress(position + 1)
    }

    private fun showSnackbar(message: String, intent: Intent, onDismissed: () -> Unit) {
        val snackbar = Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setDuration(1500)
            .setBackgroundTint(Color.parseColor("#5FB21A"))
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                startActivity(intent)
                onDismissed()
            }
        })
        snackbar.show()
    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    private fun showProgressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        nextButton.isEnabled = !show
        if (show) {
            nextButton.text = ""
        } else {
            nextButton.text = getString(R.string.btn_txt_next)
        }
    }
}
