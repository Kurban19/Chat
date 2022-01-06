package com.envyglit.main.di.component

import com.envyglit.main.di.module.MainModule
import dagger.Component

@Component(modules = [MainModule::class])
interface MainComponent
