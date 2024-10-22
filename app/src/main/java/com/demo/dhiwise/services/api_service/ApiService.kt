package com.demo.dhiwise.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class OtpRequest(
    val contact_number: String
)


data class OtpResponse(val data: Any?, val meta: Meta)

data class Meta(val message: String)

interface ApiService {
    @POST("send-otp")
    fun requestOtp(@Body request: OtpRequest): Call<OtpResponse>
}
