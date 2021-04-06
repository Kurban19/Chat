package com.shkiper.chat

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.shkiper.chat.di.component.DaggerAppComponent
import com.shkiper.chat.utils.FireBaseUtils
import dagger.hilt.android.HiltAndroidApp
import java.util.*


@HiltAndroidApp
class App: Application(), LifecycleObserver {

    val appComponent = DaggerAppComponent.create()!!

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
    }


    companion object{
        private var instance:App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun getAppComponent(){

        }

    }

    init {
        instance = this
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        FireBaseUtils.updateCurrentUser(Date(), true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackgrounded() {
        FireBaseUtils.updateCurrentUser(Date(), false)
    }


}