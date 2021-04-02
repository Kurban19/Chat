package com.shkiper.chat

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp
import com.shkiper.chat.di.component.DaggerAppComponent
import com.shkiper.chat.interfaces.FireBaseUsers
import com.shkiper.chat.utils.FireBaseUtils
import java.util.*


@HiltAndroidApp
class App: Application() {

    val appComponent = DaggerAppComponent.create()

    override fun onCreate() {
        super.onCreate()
        FireBaseUtils.updateCurrentUser(Date(), true)
    }


    companion object{
        private var instance:App? = null

        fun applicationContext(): Context{
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }


}