package com.example.testassignment.model.bean

data class ErrorResponse(
    val errors: List<Error>?, val message: String, val status: Int?
)

//error
data class Error(
   val msg: String?,
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