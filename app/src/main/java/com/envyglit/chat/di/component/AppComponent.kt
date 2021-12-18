package com.envyglit.chat.di.component

import com.envyglit.chat.di.module.AppModule
import dagger.Component


@Component(modules = [AppModule::class])
interface AppComponent
