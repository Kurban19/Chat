package ru.skillbranch.chat.di.component

import dagger.Component
import ru.skillbranch.chat.di.module.NetworkModule
import ru.skillbranch.chat.ui.main.MainActivity
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
}