package com.example.testassignment.di

import com.example.testassignment.view.base.BaseViewModel
import com.example.testassignment.view_model.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    /**Provide ViewModel object in activity Class
     * you can use it any Activity/Fragment class  below is declaration
     *
     * In Activity
     * private val baseViewModel: BaseViewModel by viewmodel() create object in activity scope
     *
     * In Fragment
     *  private val baseViewModel: BaseViewModel by viewmodel()  create object in fragment scope
     *
     *  get object of activity scope use sharedViewModel()
     *  private val baseViewModel: BaseViewModel by sharedViewmodel()
     *  */

    viewModel { BaseViewModel<Nothing>() }
    viewModel { HomeViewModel(get()) }

}