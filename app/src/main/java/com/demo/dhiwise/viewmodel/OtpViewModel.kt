package com.demo.dhiwise.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.dhiwise.network.OtpResponse
import com.demo.dhiwise.repository.OtpRepository

class OtpViewModel : ViewModel() {
    private val otpRepository = OtpRepository()
    private val _otpResponse = MutableLiveData<OtpResponse?>()
    val otpResponse: LiveData<OtpResponse?> get() = _otpResponse

    fun requestOtp(phoneNumber: String) {
        otpRepository.requestOtp(phoneNumber) { response ->
            _otpResponse.value = response
        }
    }
}
