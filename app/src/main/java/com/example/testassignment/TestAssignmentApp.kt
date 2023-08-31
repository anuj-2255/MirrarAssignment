package com.example.testassignment

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.testassignment.di.appModule
import com.example.testassignment.di.repoModule
import com.example.testassignment.di.viewModelModule
import com.example.testassignment.model.repo.AppRepository
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

class TestAssignmentApp : Application() {
    private val appRepo: AppRepository by inject()

    override fun onCreate() {
        super.onCreate()
        instance = this
        Hawk.init(instance).build()

        /*** start Koin DI  */
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TestAssignmentApp)
            modules(getModule())
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        //  MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST, null)
        // myAppsNotificationManager = MyAppsNotificationManager.getInstance(this)
        /* myAppsNotificationManager?.registerNotificationChannelChannel(
             getString(R.string.DEFAULT_CHANNEL_ID),
             getString(R.string.CHANNEL_DEFAULT),
             getString(R.string.CHANNEL_DEFAULT_DESCRIPTION)
         )
 */
    }

    /*** function to get all di modules array*/
    private fun getModule(): List<Module> {
        return listOf(appModule, repoModule, viewModelModule)
    }


    companion object {
        lateinit var instance: TestAssignmentApp

    }


}