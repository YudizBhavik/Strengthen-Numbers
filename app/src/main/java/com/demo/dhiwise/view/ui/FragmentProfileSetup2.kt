package com.demo.dhiwise.view.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.demo.dhiwise.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class FragmentProfileSetup2 : Fragment() {
    private lateinit var imgProfileSetup: ImageView
    private val PICK_IMAGE_REQUEST = 1
    private val CAMERA_REQUEST = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_setup2, container, false)
        imgProfileSetup = view.findViewById(R.id.img_profile_setup2)
        imgProfileSetup.setOnClickListener { showBottomSheet() }
        return view
    }

    private fun showBottomSheet() {
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
