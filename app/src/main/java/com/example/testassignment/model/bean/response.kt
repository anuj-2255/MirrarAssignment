package com.example.testassignment.model.bean

data class ErrorResponse(
    val error: Error?
)

data class Error(
    val code: String?,
    val message: String?
)


//Response Data class
data class NasaResponse(
    val date: String?,
    val explanation: String?,
    val hdurl: String?,
    val media_type: String?,
    val service_version: String?,
    val title: String?,
    val url: String?
)