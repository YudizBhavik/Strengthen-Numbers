package com.demo.dhiwise.repository

import android.util.Log
import com.demo.dhiwise.network.OtpRequest
import com.demo.dhiwise.network.OtpResponse
import com.demo.dhiwise.services.api_service.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpRepository {
    fun requestOtp(phoneNumber: String, callback: (OtpResponse?) -> Unit) {
        val request = OtpRequest(contact_number = phoneNumber)
        ApiClient.apiService.requestOtp(request).enqueue(object : Callback<OtpResponse> {
            override fun onResponse(call: Call<OtpResponse>, response: Response<OtpResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Log.e("OtpRepository", "Error: ${response.code()} ${response.message()}")
                    callback(null)
                }
            }


            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}
