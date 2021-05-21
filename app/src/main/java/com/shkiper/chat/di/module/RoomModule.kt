package com.shkiper.chat.di.module

import android.app.Application
import androidx.room.Room
import com.shkiper.chat.room.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RoomModule(private val application: Application) {

    @Singleton
    @Provides
    fun providesRoomDatabase(): Database {
        return Room.databaseBuilder(application, Database::class.java, "dataBase")
            .build()
    }

}