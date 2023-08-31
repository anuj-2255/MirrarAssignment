package com.example.testassignment.di

import android.util.Log
import com.example.testassignment.TestAssignmentApp
import com.example.testassignment.network.ApiConstant
import com.example.testassignment.network.ApiServices
import com.example.testassignment.utils.CoroutineCallAdapterFactory
import com.example.testassignment.utils.Loading
import com.example.testassignment.utils.NetworkConnectionInterceptor
import com.example.testassignment.utils.ResourceProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.hawk.Hawk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    /** PROVIDE GSON SINGLETON */
    single<Gson> {
        val builder = GsonBuilder()
        builder.setLenient().create()
    }

    /** PROVIDE RETROFIT SINGLETON */
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(NetworkConnectionInterceptor(TestAssignmentApp.instance))
        httpClient.addInterceptor(loggingInterceptor)
        httpClient.connectTimeout(ApiConstant.API_TIME_OUT, TimeUnit.MILLISECONDS)
        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
            chain.proceed(request.build())
        }
        val okHttpClient = httpClient.build()

        Retrofit.Builder()
            .baseUrl(ApiConstant.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(get() as Gson))
            .build()
    }

    /*** Provide API Service Singleton*/
    single {
        (get<Retrofit>()).create(ApiServices::class.java)
    }

    single { Loading() }

    single { ResourceProvider(androidApplication()) }


}