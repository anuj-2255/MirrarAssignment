package com.example.testassignment.view.navigator

// this interface allow us to perform certain operation in activity or fragment from viewModel on the basis on api response.
interface HomeNavigator {
    fun showMsg(msg:String)
    fun showLoader()
    fun hideLoader()
    fun showHidePlayerView()

}