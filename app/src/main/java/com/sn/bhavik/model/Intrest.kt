package com.sn.bhavik.model

data class Interest(
    val imageResId: Int,
    val selectedImageResId: Int,
    val text: String,
    var isSelected: Boolean = false // Track selection state
)