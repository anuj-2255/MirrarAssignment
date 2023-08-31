package com.example.testassignment.view.base

import com.example.testassignment.model.repo.AppRepository
import java.lang.ref.WeakReference
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


open class BaseViewModel<N> : ObservableViewModel() , KoinComponent {
    /**
     *   ApplicationRepository is injected here to access application level
     *   functions & preference
     */
    private val appRepo: AppRepository by inject()
    private lateinit var mNavigator: WeakReference<N>

    fun getNavigator(): N? {
        return mNavigator.get()
    }


    fun setNavigator(navigator: N) {
        this.mNavigator = WeakReference(navigator)
    }



}