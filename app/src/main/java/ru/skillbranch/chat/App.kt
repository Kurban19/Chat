package ru.skillbranch.chat

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import ru.skillbranch.chat.di.component.DaggerAppComponent
import ru.skillbranch.chat.models.data.User


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