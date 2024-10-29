package com.sn.bhavik.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.sn.bhavik.local.PreferencesManager
import com.sn.bhavik.model.OtpRequest
import com.sn.bhavik.network.ApiResponse
import com.sn.bhavik.network.ProfileUpdateRequest
import com.sn.bhavik.network.ProfileUpdateRequestF2
import com.sn.bhavik.network.ProfileUpdateRequestF3
import com.sn.bhavik.repository.OtpRepository
import com.sn.bhavik.services.api_service.ApiClient.apiService
import kotlinx.coroutines.launch
import okhttp3.Callback
import retrofit2.Call
import retrofit2.Response

class OtpViewModel : ViewModel() {
    private val repository = OtpRepository()

    private lateinit var sharedPreferences: SharedPreferences

    private val _apiResponse = MutableLiveData<ApiResponse?>()
    val apiResponse: LiveData<ApiResponse?> get() = _apiResponse

    private val _otpResponse = MutableLiveData<ApiResponse?>()
    val otpResponse: LiveData<ApiResponse?> get() = _otpResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage



    fun verifyOtp(otp: String, phoneNumber: String) {
        val request = OtpRequest(otp, phoneNumber)
        repository.verifyOtp(request) { response, errorMessage ->
            _apiResponse.postValue(response)
            if (errorMessage != null) {
                _errorMessage.postValue(errorMessage)
            }
        }
    }


    fun requestOtp(phoneNumber: String) {
        repository.requestOtp(phoneNumber) { response ->
            _otpResponse.postValue(response)
        }
    }

    fun resendOtp(phoneNumber: String) {
        repository.resendOtp(phoneNumber) { response ->
            _otpResponse.postValue(response)
        }
    }
    fun updateProfile(request: ProfileUpdateRequest) {
        val token = PreferencesManager.getToken()
        if (token != null) {
            repository.updateProfile(request, "Bearer " + token) { response ->
                _apiResponse.postValue(response)
            }
        } else {
            _errorMessage.postValue("Token not found.")
        }
    }
    fun updateProfileF2(request: ProfileUpdateRequestF2) {
        val token = PreferencesManager.getToken()
        if (token != null) {
            repository.updateProfileF2(request, "Bearer " + token) { response ->
                _apiResponse.postValue(response)
            }
        } else {
            _errorMessage.postValue("Token not found.")
        }
    }
    fun updateProfileF3(request: ProfileUpdateRequestF3) {
        val token = PreferencesManager.getToken()
        if (token != null) {
            repository.updateProfileF3(request, "Bearer $token") { response ->
                _apiResponse.postValue(response)
            }
        } else {
            _errorMessage.postValue("Token not found.")
        }
    }


//    fun getProfile(token: String, requestBody: JsonObject) {
//        apiService.getProfile(token).enqueue(object : Callback<ApiResponse> {
//             fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//                if (response.isSuccessful) {
//                    _apiResponse.value = response.body()
//                } else {
//                    _apiResponse.value = "Error: ${response.message()}"
//
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                errorMessage.value = "Failed to fetch profile: ${t.message}"
//            }
//        })
//    }


}


