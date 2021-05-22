package com.shkiper.chat.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.shkiper.chat.room.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun providesRoomDatabase(@ApplicationContext context: Context): Database
            = Room.databaseBuilder(context, Database::class.java,"Database").build()
}