package com.example.testassignment.network

import androidx.lifecycle.MutableLiveData
import com.example.testassignment.model.bean.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    val msg = error?.error?.message ?: "Something went wrong"
                    val code = error?.error?.code ?: ""
                    responseLiveData?.postValue(
                        ApiResponse.error(
                            ApiResponse.ApiError(
                                response.code(),
                                msg,
                                code
                            )
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