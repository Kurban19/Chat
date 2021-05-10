package com.shkiper.chat.di.component

import com.shkiper.chat.di.module.NetworkModule
import com.shkiper.chat.firebase.FireBaseServiceImpl
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.repositories.MainRepository
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun getMainRepository(): MainRepository {
        return NetworkModule.providesMainRepository(FireBaseServiceImpl())
    }

}
