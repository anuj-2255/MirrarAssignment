package com.example.testassignment.model.bean





data class PhoneNumberLoginRequest(
    var number: String? = null,
)

data class VerifyOtpRequest(
    var number: String? = null,
    var otp: String? = null
)
