package com.example.testassignment.network


import com.example.testassignment.model.bean.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @GET("apod")
    fun getData(@Query("api_key") apiKey: String? = null): Deferred<Response<NasaResponse>>


}