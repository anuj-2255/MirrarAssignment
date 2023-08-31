package com.example.testassignment.utils

import android.content.Context
import com.orhanobut.hawk.Hawk
import com.orhanobut.hawk.NoEncryption

object HawkWrapper {
        fun init(context: Context?) {
            val encryption = NoEncryption()
            Hawk.init(context).setEncryption(encryption).build()
        }
    }