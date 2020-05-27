package ru.skillbranch.chat

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.chat.models.data.User
import ru.skillbranch.chat.repositories.PreferencesRepository

class App: Application() {

    companion object{

        var user = User.makeUser("Admin")
        private var instance:App? = null

        fun applicationContext(): Context{
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        PreferencesRepository.getAppTheme().also {
            AppCompatDelegate.setDefaultNightMode(it)
        }
    }
}