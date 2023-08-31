package com.example.testassignment.model.repo

import androidx.lifecycle.MutableLiveData
import com.example.testassignment.model.bean.NasaResponse
import com.example.testassignment.network.ApiConstant.Companion.NASA_API_KEY
import com.example.testassignment.network.ApiResponse
import com.example.testassignment.network.ApiServices
import com.example.testassignment.network.DataFetchCall
import kotlinx.coroutines.Deferred
import retrofit2.Response

class AppRepository(private val apiServices: ApiServices) {


    fun getData(response: MutableLiveData<ApiResponse<NasaResponse>>) {
        object : DataFetchCall<NasaResponse>(response) {
            override suspend fun createCallAsync(): Deferred<Response<NasaResponse>> {
                return apiServices.getData(NASA_API_KEY)
            }
        }.execute()
    }
}


