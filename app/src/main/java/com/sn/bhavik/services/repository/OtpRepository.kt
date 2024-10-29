package com.sn.bhavik.repository

import android.util.Log
import com.sn.bhavik.local.PreferencesManager
import com.sn.bhavik.model.OtpRequest
import com.sn.bhavik.network.ApiResponse
import com.sn.bhavik.network.ProfileUpdateRequest
import com.sn.bhavik.network.ProfileUpdateRequestF2
import com.sn.bhavik.network.ProfileUpdateRequestF3
import com.sn.bhavik.services.api_service.ApiClient
import com.google.gson.Gson
import com.google.gson.JsonObject
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
                    val token = response.headers().get("X-Authorization-Token").toString()
                    Log.d("Token", token)
                    PreferencesManager.saveToken(token)

                    callback(response.body(), null)
                }
                else {
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

    fun updateProfile(request: ProfileUpdateRequest, token: String, callback: (ApiResponse?) -> Unit) {
        val gson = Gson()
        val jsonObject: JsonObject = gson.toJsonTree(request).asJsonObject

        ApiClient.apiService.updateProfile("bareer" + token, jsonObject).enqueue(object : Callback<ApiResponse> {
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
    fun updateProfileF2(request: ProfileUpdateRequestF2, token: String, callback: (ApiResponse?) -> Unit) {
        val gson = Gson()
        val jsonObject: JsonObject = gson.toJsonTree(request).asJsonObject

        ApiClient.apiService.updateProfile("bareer" + token, jsonObject).enqueue(object : Callback<ApiResponse> {
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
    fun updateProfileF3(request: ProfileUpdateRequestF3, token: String, callback: (ApiResponse?) -> Unit) {
        val gson = Gson()
        val jsonObject: JsonObject = gson.toJsonTree(request).asJsonObject

        ApiClient.apiService.updateProfile(token, jsonObject).enqueue(object : Callback<ApiResponse> {
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

    fun getProfile(callback: (ApiResponse?, String?) -> Unit) {
        val token = PreferencesManager.getToken()
        if (token.isNullOrEmpty()) {
            callback(null, "Token is missing.")
            return
        }

        ApiClient.apiService.getProfile("Bearer $token").enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, ApiResponse::class.java)
                    val errorMessage = errorResponse.meta?.message ?: "An unknown error occurred."
                    callback(null, errorMessage)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }


}


