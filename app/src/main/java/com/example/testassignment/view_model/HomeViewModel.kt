package com.example.testassignment.view_model

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.testassignment.NASA_RESPONSE
import com.example.testassignment.R
import com.example.testassignment.model.bean.NasaResponse
import com.example.testassignment.model.repo.AppRepository
import com.example.testassignment.network.ApiResponse
import com.example.testassignment.utils.ResourceProvider
import com.example.testassignment.view.base.BaseViewModel
import com.example.testassignment.view.navigator.HomeNavigator
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import org.koin.core.component.inject

class HomeViewModel(val repo: AppRepository) : BaseViewModel<HomeNavigator>() {

    private val res: ResourceProvider by inject()

    //using this variable to set data in out view using databinding.
    @Bindable
    var nasaObj: NasaResponse? = if (Hawk.contains(NASA_RESPONSE)) Gson().fromJson(
        Hawk.get<String>(NASA_RESPONSE),
        NasaResponse::class.java
    ) else null
        set(value) {
            if (field != value) {
                field = value
                notifyChange()
                getNavigator()?.showHidePlayerView()
            }
        }


    //using live for observe and update data
    private val nasaResponse by lazy {
        MutableLiveData<ApiResponse<NasaResponse>>()
    }

    private fun getNasaResponse(): LiveData<ApiResponse<NasaResponse>> {
        return nasaResponse
    }

    //using observer to update our live data and return it.
    private val nasaObserver: Observer<ApiResponse<NasaResponse>> by lazy {
        Observer<ApiResponse<NasaResponse>> {
            when (it.status) {
                ApiResponse.Status.LOADING -> {
                    getNavigator()?.showLoader()
                }
                ApiResponse.Status.SUCCESS -> {
                    getNavigator()?.hideLoader()
                    it.response?.let {
                        //set data to out variable.
                        nasaObj = it
                        //putting api response in to sharedPreference using HAWK lib.
                        Hawk.put(NASA_RESPONSE, Gson().toJson(it))
                        //to make visible or hide the media player as per response.
                        getNavigator()?.showHidePlayerView()
                    }

                }
                ApiResponse.Status.ERROR -> {
                    getNavigator()?.hideLoader()
                    //we can also show the code instead of full error message coming in api.
                    //or we can put checks on 'stringCode' key to set our custom messages.
                    //but currently i am showing the same message which i got in api response
                    getNavigator()?.showMsg(
                        it.error?.message ?: res.getString(R.string.something_went_wrong)
                    )
                }
            }
        }
    }

    //for making api call
    fun getDailyImage() {
        repo.getData(nasaResponse)
        getNasaResponse().observeForever(nasaObserver)
    }

}