package com.example.testassignment.network

import androidx.lifecycle.MutableLiveData
import com.example.testassignment.model.bean.ErrorResponse
import com.google.gson.Gson
import com.happytaxidriver.*
import kotlinx.coroutines.*
import retrofit2.Response

abstract class DataFetchCall<ResultType>(private val responseLiveData: MutableLiveData<ApiResponse<ResultType>>? = null) {


    abstract suspend fun createCallAsync(): Deferred<Response<ResultType>>

    fun execute() {
        responseLiveData?.postValue(ApiResponse.loading())
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = createCallAsync().await()
                if (response.isSuccessful) {
                    responseLiveData?.postValue(ApiResponse.success(response.body()))
                } else if (response.code() == 401) {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    val msg = error?.errors?.firstOrNull()?.msg ?: error.message
                    responseLiveData?.postValue(
                        ApiResponse.error(
                            error.message?.let {
                                ApiResponse.ApiError(
                                    response.code(),
                                    msg,
                                    it
                                )
                            }
                        )
                    )
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                responseLiveData?.postValue(
                    ApiResponse.error(
                        ApiResponse.ApiError(
                            500,
                            exception.message ?: "Something went wrong"
                        )
                    )
                )
            }
        }
    }
}