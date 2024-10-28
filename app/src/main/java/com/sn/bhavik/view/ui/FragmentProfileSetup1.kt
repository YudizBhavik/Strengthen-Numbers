package com.sn.bhavik.view.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sn.bhavik.R
import com.sn.bhavik.network.ProfileUpdateRequest
import com.sn.bhavik.viewmodel.OtpViewModel
import com.google.android.material.textfield.TextInputEditText

class FragmentProfileSetup1 : Fragment() {

    private val otpViewModel: OtpViewModel by activityViewModels()
    private lateinit var editFullName: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editDob: TextInputEditText
    internal var onProfileUpdateSuccess: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_setup1, container, false)

        editFullName = view.findViewById(R.id.edit_full_name_text)
        editEmail = view.findViewById(R.id.edit_email_text)
        editDob = view.findViewById(R.id.edit_dob_text)

        editDob.setOnClickListener {
            showDatePicker()
        }

        setupObservers()

        return view
    }

    internal fun updateProfile() {
        val fullName = editFullName.text.toString()
        val email = editEmail.text.toString()
        val dob = editDob.text.toString()

        if (fullName.isNotBlank() && email.isNotBlank() && dob.isNotBlank()) {
            val request = ProfileUpdateRequest(fullName, email, dob)
            otpViewModel.updateProfile(request)
            otpViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    Toast.makeText(context, "Profile updated successfully! updateProfile", Toast.LENGTH_SHORT).show()
                    onProfileUpdateSuccess?.invoke()
                } else {
                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        otpViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
//                Toast.makeText(context, "Profile updated successfully! observeViewModel", Toast.LENGTH_SHORT).show()
                onProfileUpdateSuccess?.invoke()
            } else {
                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }

        otpViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupObservers() {
        observeViewModel()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedMonth + 1}-$selectedDay-$selectedYear"
                editDob.setText(formattedDate)
            }, year, month, day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showSnackbar(message: String, intent: Intent, onDismissed: () -> Unit) {
        val snackbar = view?.let {
            Snackbar.make(it.findViewById(R.id.main), message, Snackbar.LENGTH_SHORT)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setDuration(1500)
                .setBackgroundTint(Color.parseColor("#5FB21A"))
        }
        snackbar?.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                startActivity(intent)
                onDismissed()
            }
        })
        snackbar?.show()
    }

    private fun showErrorSnackbar(message: String, onDismissed: () -> Unit = {}) {
        val snackbar = view?.let {
            Snackbar.make(it.findViewById(R.id.main), message, Snackbar.LENGTH_SHORT)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(Color.parseColor("#FF0000"))
        } 
        snackbar?.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                onDismissed()
            }
        })
        snackbar?.show()
    }
}
