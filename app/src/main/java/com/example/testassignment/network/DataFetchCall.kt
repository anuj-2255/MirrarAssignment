package com.example.testassignment.network

import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.example.testassignment.R
import com.example.testassignment.TestAssignmentApp
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
        val instance = TestAssignmentApp.instance
        responseLiveData?.postValue(ApiResponse.loading())
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = createCallAsync().await()
                if (response.isSuccessful) {
                    responseLiveData?.postValue(ApiResponse.success(response.body()))
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    //you can also create your custom error message on the basis ErrorCode you get in API.
                    val code = error?.error?.code ?: instance.getString(R.string.something_went_wrong)
                    val msg = error?.error?.message ?: instance.getString(R.string.something_went_wrong)
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
                            exception.message ?: instance.getString(R.string.something_went_wrong)
                        )
                    )
                )
            }
        }
    }
}