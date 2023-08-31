package com.example.testassignment.utils

import com.example.testassignment.R
import com.example.testassignment.TestAssignmentApp
import java.io.IOException

class NoConnectivityException : IOException() {
    // You can send any message whatever you want from here.


    override val message: String
        get() = TestAssignmentApp.instance.getString(R.string.no_internet)


    // You can send any message whatever you want from here.
}