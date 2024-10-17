package com.demo.dhiwise

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Typeface
import android.os.CountDownTimer
import android.widget.Button
import com.google.android.material.button.MaterialButton

class OtpVerificationScreen : AppCompatActivity() {
    private lateinit var txtSentTheCode: TextView
    private lateinit var txtDidNotReceive: TextView
    private lateinit var txtResend: TextView
    private lateinit var img_icon: ImageButton
    private val resendOtpMessage = "Didn’t receive OTP? %d secs"

    private lateinit var btn_verify : MaterialButton

    private lateinit var edit_otp_1: EditText
    private lateinit var edit_otp_2: EditText
    private lateinit var edit_otp_3: EditText
    private lateinit var edit_otp_4: EditText

    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 30000 // 30 seconds

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            val otpInput = s.toString()

//            if (edit_otp_1.length() != 0){
//                edit_otp_2.requestFocus()
//            }

            if (edit_otp_1.length() !=0) edit_otp_2.requestFocus()
            if (edit_otp_2.length() !=0) edit_otp_3.requestFocus()
            if (edit_otp_3.length() !=0) edit_otp_4.requestFocus()



//            when {
//                otpInput.length == 0 -> edit_otp_1.requestFocus()
//                otpInput.length == 1 -> edit_otp_2.requestFocus()
//                otpInput.length == 2 -> edit_otp_3.requestFocus()
//                otpInput.length == 3 -> edit_otp_4.requestFocus()
//                else -> {}
//            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        setContentView(R.layout.activity_otp_verification_screen)

        btn_verify = findViewById(R.id.btn_verify)

        edit_otp_1 = findViewById(R.id.edit_otp_1)
        edit_otp_2 = findViewById(R.id.edit_otp_2)
        edit_otp_3 = findViewById(R.id.edit_otp_3)
        edit_otp_4 = findViewById(R.id.edit_otp_4)

        edit_otp_1.addTextChangedListener(textWatcher)
        edit_otp_2.addTextChangedListener(textWatcher)
        edit_otp_3.addTextChangedListener(textWatcher)
        edit_otp_4.addTextChangedListener(textWatcher)

        val phoneNumber = intent.getStringExtra("PHONE_NUMBER") ?: ""
        val message = "We have sent the verification code to your\n$phoneNumber mobile number."
        val spannableString = SpannableString(message)
        val start = message.indexOf(phoneNumber)
        val end = start + phoneNumber.length

        img_icon = findViewById(R.id.back_icon)
        img_icon.setOnClickListener { finish() }

        btn_verify.setOnClickListener {
            intent = Intent(this,LocationPermissionScreen::class.java)
            startActivity(intent)
        }

        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        txtSentTheCode = findViewById(R.id.txt_sent_the_code)
        txtSentTheCode.text = spannableString

        txtDidNotReceive = findViewById(R.id.txt_did_not_received)
        txtResend = findViewById(R.id.txt_resend)

        startTimer()

        txtResend.setOnClickListener { startTimer() }

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
                txtResend.setTextColor(resources.getColor(R.color.S19, theme))
            }
        }.start()
    }
}
