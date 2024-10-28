package com.demo.dhiwise.viewmodel

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.dhiwise.local.PreferencesManager
import com.demo.dhiwise.model.OtpRequest
import com.demo.dhiwise.network.ApiResponse
import com.demo.dhiwise.network.ProfileUpdateRequest
import com.demo.dhiwise.network.ProfileUpdateRequestF2
import com.demo.dhiwise.network.ProfileUpdateRequestF3
import com.demo.dhiwise.repository.OtpRepository

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



}


