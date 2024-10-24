package com.demo.dhiwise.repository

import android.util.Log
import com.demo.dhiwise.model.OtpRequest
import com.demo.dhiwise.network.ApiResponse
import com.demo.dhiwise.services.api_service.ApiClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpRepository {
    fun requestOtp(phoneNumber: String, callback: (ApiResponse?) -> Unit) {
        val request = OtpRequest(contact_number = phoneNumber, otp = "")
        ApiClient.apiService.requestOtp(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Log.e("OtpRepository", "Error: ${response.code()} ${response.message()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("OtpRepository", "Failure: ${t.message}")
                callback(null)
            }
        })
    }
    fun verifyOtp(request: OtpRequest, callback: (ApiResponse?, String?) -> Unit) {
        ApiClient.apiService.verifyOtp(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("OtpRepository", "Error: ${response.code()} ${response.message()} - Body: $errorBody")

                    val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
                    val errorMessage = errorResponse.meta?.message ?: "An unknown error occurred."

                    callback(null, errorMessage)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("OtpRepository", "Failure: ${t.message}")
                callback(null, t.message)
            }
        })
    }

    fun resendOtp(phoneNumber: String, callback: (ApiResponse?) -> Unit) {
        ApiClient.apiService.resendOtp(OtpRequest("", phoneNumber)).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Log.e("OtpRepository", "Error: ${response.code()} ${response.message()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("OtpRepository", "Failure: ${t.message}")
                callback(null)
            }
        })
    }

}

