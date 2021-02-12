package ru.skillbranch.chat

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ru.skillbranch.chat.models.data.User

class App: Application() {

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