package com.demo.dhiwise.view.ui

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.demo.dhiwise.R
import com.demo.dhiwise.network.ProfileUpdateRequest
import com.demo.dhiwise.viewmodel.OtpViewModel
import com.google.android.material.textfield.TextInputEditText

class FragmentProfileSetup1 : Fragment() {

    private val otpViewModel: OtpViewModel by activityViewModels()

    private lateinit var editFullName: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editDob: TextInputEditText

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

        return view
    }

//    internal fun getProfileData(): ProfileUpdateRequest? {
//        val fullName = editFullName.text.toString()
//        val email = editEmail.text.toString()
//        val dob = editDob.text.toString()
//
//        return if (fullName.isNotBlank() && email.isNotBlank() && dob.isNotBlank()) {
//            ProfileUpdateRequest(fullName, email, dob)
//        } else {
//            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
//            null
//        }
//    }
    internal fun updateProfile() {
        val fullName = editFullName.text.toString()
        val email = editEmail.text.toString()
        val dob = editDob.text.toString()

        if (fullName.isNotBlank() && email.isNotBlank() && dob.isNotBlank()) {
            val request = ProfileUpdateRequest(fullName, email, dob)
//            otpViewModel.updateProfile(request)
        } else {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        otpViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            } ?: run {
                Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        }

        otpViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
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
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editDob.setText(formattedDate)
            }, year, month, day
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}
