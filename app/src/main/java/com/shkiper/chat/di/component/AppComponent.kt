package com.shkiper.chat.di.component

import dagger.Component
import com.shkiper.chat.di.module.NetworkModule
import com.shkiper.chat.ui.main.MainActivity
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
}