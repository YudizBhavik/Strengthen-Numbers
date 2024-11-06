package com.sn.bhavik.view.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sn.bhavik.R
import com.sn.bhavik.model.Interest
import com.sn.bhavik.network.ProfileUpdateRequestF3
import com.sn.bhavik.view.adapter.GridviewAdapter
import com.sn.bhavik.viewmodel.OtpViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject

class FragmentProfileSetup3 : Fragment() {

    private lateinit var gridView: GridView
    private lateinit var adapter: GridviewAdapter
    private var selectedFitnessLevel: String? = null
    private lateinit var fitnessLevelEditText: TextInputEditText

    private val otpViewModel: OtpViewModel by activityViewModels()

    private val interests = listOf(
        Interest(R.drawable.gym, R.drawable.gym_selected, "Strength Training"),
        Interest(R.drawable.yoga, R.drawable.yoga_selected, "Mind/Body"),
        Interest(R.drawable.running, R.drawable.running_selected, "Cardio"),
        Interest(R.drawable.home_workout, R.drawable.home_workout_selected, "Rehab/Recovery"),
        Interest(R.drawable.cycling, R.drawable.cycling_selected, "Outdoor Activities"),
        Interest(R.drawable.running, R.drawable.running_selected, "Group Classes"),
        Interest(R.drawable.diet, R.drawable.diet_selected, "Nutrition"),
        Interest(R.drawable.sports, R.drawable.sports_selected, "Sports"),
        Interest(R.drawable.cardio, R.drawable.cardio_selected, "HIIT")
    )

    var onProfileUpdateSuccess: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_setup3, container, false)

        gridView = view.findViewById(R.id.gridView)
        adapter = GridviewAdapter(requireContext(), interests)
        gridView.adapter = adapter
        fitnessLevelEditText = view.findViewById(R.id.edit_fitness_level_text)

        fitnessLevelEditText.setOnClickListener {
            showFitnessLevelBottomSheet(fitnessLevelEditText)
        }

        setupObservers()
        fetchProfileData()

        return view
    }

    private fun fetchProfileData() {
        val requestBody = JsonObject() // Pass any required data here
        otpViewModel.getProfile(requestBody)
    }

    private fun setupObservers() {
        otpViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Log.d("ProfileSetup", "API Response: $response")
                response.data?.let { userData ->
                    Log.d("ProfileSetup", "User Data: $userData")
                    selectedFitnessLevel = userData.fitnessLevel
                    fitnessLevelEditText.setText(selectedFitnessLevel)

                    val selectedInterests = userData.interests ?: emptyList()
                    val updatedInterests = interests.map { interest ->
                        interest.copy(isSelected = selectedInterests.contains(interest.text))
                    }
                    adapter.updateInterests(updatedInterests)
                }
            } else {
                showErrorSnackbar("Failed to fetch profile")
            }
        }

        otpViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                val successMessage = response.meta?.message ?: "Profile updated successfully!"
                Log.d("Response", response.data.toString())
//                showSnackbar(successMessage)
                onProfileUpdateSuccess?.invoke()
            } else {
                showErrorSnackbar("Response was null or update failed.")
            }
        }
    }

    private fun showFitnessLevelBottomSheet(editText: TextInputEditText) {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.fitness_bottomsheet, null)

        val beginner: TextView = bottomSheetView.findViewById(R.id.text_beginner)
        val intermediate: TextView = bottomSheetView.findViewById(R.id.text_intermediate)
        val advanced: TextView = bottomSheetView.findViewById(R.id.text_advanced)

        beginner.setOnClickListener {
            selectedFitnessLevel = "beginner"
            editText.setText(selectedFitnessLevel)
            bottomSheetDialog.dismiss()
        }

        intermediate.setOnClickListener {
            selectedFitnessLevel = "intermediate"
            editText.setText(selectedFitnessLevel)
            bottomSheetDialog.dismiss()
        }

        advanced.setOnClickListener {
            selectedFitnessLevel = "advanced"
            editText.setText(selectedFitnessLevel)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    internal fun updateProfile(): Boolean {
        val selectedInterests = adapter.getSelectedInterests()
        if (selectedInterests.size < 3) {
            showSnackbar("Please select at least 3 interests")
            return false
        }
        if (selectedFitnessLevel.isNullOrBlank()) {
            showSnackbar("Please enter your fitness level")
            return false
        }

        Log.d("UpdateProfile", "Selected Fitness Level: $selectedFitnessLevel")
        Log.d("UpdateProfile", "Selected Interests: $selectedInterests")

        val request = ProfileUpdateRequestF3(selectedFitnessLevel!!, selectedInterests)

        Log.d("UpdateProfile", "Request: $request")
        otpViewModel.updateProfileF3(request)

        return true
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
                .setBackgroundTint(Color.parseColor("#D32F2F"))
        }
        snackbar?.show()
    }
}
