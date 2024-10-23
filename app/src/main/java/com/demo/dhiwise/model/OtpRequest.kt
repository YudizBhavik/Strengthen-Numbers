package com.demo.dhiwise.model

import com.google.gson.annotations.SerializedName

data class OtpRequest(
    @SerializedName("otp") val otp: String,
    @SerializedName("contact_number") val contact_number: String
)
