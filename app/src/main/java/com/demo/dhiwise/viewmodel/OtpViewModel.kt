package com.demo.dhiwise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.dhiwise.model.OtpRequest
import com.demo.dhiwise.network.ApiResponse
import com.demo.dhiwise.repository.OtpRepository

class OtpViewModel : ViewModel() {
    private val repository = OtpRepository()

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

}


//package com.demo.dhiwise.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.demo.dhiwise.model.OtpRequest
//import com.demo.dhiwise.network.ApiResponse
//import com.demo.dhiwise.repository.OtpRepository
//
//class OtpViewModel : ViewModel() {
//    private val repository = OtpRepository()
//
//    private val _apiResponse = MutableLiveData<ApiResponse?>()
//    val apiResponse: LiveData<ApiResponse?> get() = _apiResponse
//
//    private val _errorMessage = MutableLiveData<String?>()
//    val errorMessage: LiveData<String?> get() = _errorMessage
//
//    fun updateProfile(request: ProfileUpdateRequest) {
//        repository.updateProfile(request) { response, error ->
//            _apiResponse.postValue(response)
//            if (error != null) {
//                _errorMessage.postValue(error)
//            }
//        }
//    }
//}