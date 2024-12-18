package com.sn.bhavik.view.ui

import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sn.bhavik.R
import com.sn.bhavik.network.ProfileUpdateRequest
import com.sn.bhavik.viewmodel.OtpViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.sn.bhavik.local.PreferencesManager

data class ValidationResult(val isValid: Boolean, val errorMessages: Map<String, String>)

class FragmentProfileSetup1 : Fragment() {

    private val otpViewModel: OtpViewModel by activityViewModels()
    private lateinit var editFullName: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editDob: TextInputEditText
    private lateinit var checkBox1: MaterialCheckBox  // Change to CheckBox
    private lateinit var checkBox2: MaterialCheckBox  // Change to CheckBox
    internal var onProfileUpdateSuccess: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_setup1, container, false)

        editFullName = view.findViewById(R.id.edit_full_name_text)
        editEmail = view.findViewById(R.id.edit_email_text)
        editDob = view.findViewById(R.id.edit_dob_text)
        checkBox1 = view.findViewById(R.id.t_c_checkBox1) // Initialize your checkbox
        checkBox2 = view.findViewById(R.id.year_plus_checkBox) // Initialize your checkbox

        editDob.setOnClickListener {
            showDatePicker()
        }

        fetchUserProfile()
        setupObservers()

        return view
    }

    private fun fetchUserProfile() {
        val token = PreferencesManager.getToken()
        Log.d("Token Fetch", "fetchUserProfile: $token")
        if (token != null) {
            val requestBody = JsonObject()
            otpViewModel.getProfile(requestBody)
        } else {
            showErrorSnackbar("Authentication token is missing.")
        }
    }

    private fun setupObservers() {
        otpViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("ProfileSetup", "API Response: $response")

                response.data?.let { userData ->
                    Log.d("ProfileSetup", "User Data: $userData")
                    editFullName.setText(userData.name)
                    editEmail.setText(userData.email)
                    editDob.setText(userData.dob)
                    onProfileUpdateSuccess?.invoke()
                }
            } else {
                showErrorSnackbar("Failed to fetch profile")
            }
        }

        otpViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                val successMessage = response.meta?.message ?: "Profile updated successfully!"
                Log.d("Response", response.data.toString())
            } else {
                showErrorSnackbar("Response was null.")
            }
        }
    }

    fun updateProfile(): Boolean {
        clearErrorMessages()

        val fullName = editFullName.text.toString()
        val email = editEmail.text.toString()
        val dob = editDob.text.toString()
        val validationResult = validateInputs(fullName, email, dob)
        val isCheckbox1Checked = checkBox1.isChecked
        val isCheckbox2Checked = checkBox2.isChecked
        if (!isCheckbox1Checked || !isCheckbox2Checked) {
            showErrorSnackbar("Please agree to the terms and conditions")
            return false
        }

        if (validationResult.isValid) {
            val request = ProfileUpdateRequest(fullName, email, dob)
            otpViewModel.updateProfile(request)
            observeViewModel()
            return true
        } else {
            validationResult.errorMessages.forEach { (field, message) ->
                showError(field, message)
            }
            return false
        }
    }

    private fun validateInputs(fullName: String, email: String, dob: String): ValidationResult {
        val errorMessages = mutableMapOf<String, String>()
        var isValid = true

        if (fullName.isBlank()) {
            isValid = false
            errorMessages["fullname"] = getString(R.string.error_required_name)
        }

        if (email.isBlank()) {
            isValid = false
            errorMessages["email"] = getString(R.string.error_required_email)
        }

        if (dob.isBlank()) {
            isValid = false
            errorMessages["dob"] = getString(R.string.error_required_dob)
        }

        return ValidationResult(isValid, errorMessages)
    }

    private fun clearErrorMessages() {
        view?.findViewById<TextView>(R.id.tv_error_message_fullname)?.visibility = View.GONE
        view?.findViewById<TextView>(R.id.tv_error_message_email)?.visibility = View.GONE
        view?.findViewById<TextView>(R.id.tv_error_message_dob)?.visibility = View.GONE
    }

    private fun showError(field: String, message: String) {
        when (field) {
            "fullname" -> view?.findViewById<TextView>(R.id.tv_error_message_fullname)?.apply {
                text = message
                visibility = View.VISIBLE
            }
            "email" -> view?.findViewById<TextView>(R.id.tv_error_message_email)?.apply {
                text = message
                visibility = View.VISIBLE
            }
            "dob" -> view?.findViewById<TextView>(R.id.tv_error_message_dob)?.apply {
                text = message
                visibility = View.VISIBLE
            }
        }
    }

    private fun observeViewModel() {
        otpViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                onProfileUpdateSuccess?.invoke()
                showSnackbar("User Details updated successfully!")
            } else {
                showErrorSnackbar("Failed to update profile")
            }
        }

        otpViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
//                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
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

    private fun showSnackbar(message: String) {
        val snackbar = view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(Color.parseColor("#5FB21A"))
        }
        snackbar?.show()
    }

    private fun showErrorSnackbar(message: String) {
        val snackbar = view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(Color.parseColor("#FF0000"))
        }
        snackbar?.show()
    }
}
