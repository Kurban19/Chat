package com.shkiper.chat

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import com.shkiper.chat.di.component.DaggerAppComponent


@HiltAndroidApp
class App: Application() {

    val appComponent = DaggerAppComponent.create()


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