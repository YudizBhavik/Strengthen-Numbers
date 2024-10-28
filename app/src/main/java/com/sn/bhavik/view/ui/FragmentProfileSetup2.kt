package com.sn.bhavik.view.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.sn.bhavik.network.ProfileUpdateRequestF2
import com.sn.bhavik.viewmodel.OtpViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.sn.bhavik.R

class FragmentProfileSetup2 : Fragment() {

    private val otpViewModel: OtpViewModel by activityViewModels()

    private lateinit var editUsername: TextInputEditText
    private lateinit var editBio: TextInputEditText
    private lateinit var editGender: TextInputEditText
    private lateinit var imgProfileSetup: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2

    private var selectedGender: String? = null

    internal var onProfileUpdateSuccess: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_setup2, container, false)

        editUsername = view.findViewById(R.id.edit_user_name_text)
        editGender = view.findViewById(R.id.edit_txt_gender)
        editBio = view.findViewById(R.id.edit_bio_text) // Change to the correct EditText

        imgProfileSetup = view.findViewById(R.id.img_profile_setup2) // Ensure this is initialized

        editGender.setOnClickListener { showGenderSelectionBottomSheet() }
        imgProfileSetup.setOnClickListener { showImageSelectBottomSheet() }

        setupObservers()

        return view
    }

    internal fun updateProfile() {
        val username = editUsername.text.toString()
        val bio = editBio.text.toString() // Changed to reflect bio instead of email

        if (username.isNotBlank() && bio.isNotBlank() && selectedGender!!.isNotBlank()) {
            Log.d("Update Profile", "updateProfile: $editUsername")
            Log.d("Update Profile", "updateProfile: $editBio")
            Log.d("Update Profile", "updateProfile: $selectedGender")
            val request = ProfileUpdateRequestF2(username, bio, selectedGender!!)
            otpViewModel.updateProfileF2(request)
        } else {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        otpViewModel.apiResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
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

    private fun showGenderSelectionBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.botttomsheet, null)

        val textMale: TextView = bottomSheetView.findViewById(R.id.text_male)
        val textFemale: TextView = bottomSheetView.findViewById(R.id.text_female)

        textMale.setOnClickListener {
            selectedGender = "male"
            editGender.setText(selectedGender)
            bottomSheetDialog.dismiss()
        }

        textFemale.setOnClickListener {
            selectedGender = "female"
            editGender.setText(selectedGender)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun showImageSelectBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.fragment_image_select_bottom_sheet, null)

        val textGallery: TextView = bottomSheetView.findViewById(R.id.text_gallery)
        val textCamera: TextView = bottomSheetView.findViewById(R.id.text_camera)

        textGallery.setOnClickListener {
            openGallery()
            bottomSheetDialog.dismiss()
        }

        textCamera.setOnClickListener {
            openCamera()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            imgProfileSetup.setImageURI(imageUri)
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            imgProfileSetup.setImageURI(imageUri)
        }
    }
}
