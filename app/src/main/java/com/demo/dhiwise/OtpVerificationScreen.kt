package com.demo.dhiwise

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Typeface
import android.os.CountDownTimer
import android.widget.ImageButton

class OtpVerificationScreen : AppCompatActivity() {
    private lateinit var txtSentTheCode: TextView
    private lateinit var txtDidNotReceive: TextView
    private lateinit var txtResend: TextView
    private lateinit var img_icon : ImageButton
    private val resendOtpMessage = "Didn’t receive OTP? %d secs"

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 30000 // 30 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.activity_otp_verification_screen)

        val phoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""

        val message = "We have sent the verification code to your\n$phoneNumber mobile number."
        val spannableString = SpannableString(message)
        val start = message.indexOf(phoneNumber)
        val end = start + phoneNumber.length
        img_icon.setOnClickListener {
            finish()
        }

        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        txtSentTheCode = findViewById(R.id.txt_sent_the_code)
        txtSentTheCode.text = spannableString

        txtDidNotReceive = findViewById(R.id.txt_did_not_received)
        txtResend = findViewById(R.id.txt_resend)


        startTimer()

        txtResend.setOnClickListener {
            startTimer()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun startTimer() {
        timeLeftInMillis = 30000
        txtResend.isEnabled = false
        txtResend.setTextColor(resources.getColor(R.color.button_disable, theme))

        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val secondsRemaining = (millisUntilFinished / 1000).toInt()
                txtDidNotReceive.text = String.format(resendOtpMessage, secondsRemaining)
            }
            override fun onFinish() {
                txtDidNotReceive.text = "Didn’t receive OTP? 0 secs"
                txtResend.isEnabled = true
                txtResend.setTextColor(resources.getColor(R.color.button_primary_foreground, theme))
            }
        }.start()
    }
}
