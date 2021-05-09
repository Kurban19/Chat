package com.shkiper.chat

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.shkiper.chat.di.component.AppComponent
import com.shkiper.chat.di.component.DaggerAppComponent
import com.shkiper.chat.utils.FireBaseUtils
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.hilt.android.HiltAndroidApp
import java.util.*


@HiltAndroidApp
class App: Application(), LifecycleObserver {


    val appComponent = DaggerAppComponent.create()

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
    }

    companion object{
        private var instance:App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
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

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onAppDestroyed(){
        FireBaseUtils.updateCurrentUser(Date(), false)
    }
}