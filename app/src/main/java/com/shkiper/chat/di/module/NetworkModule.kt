package com.shkiper.chat.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.shkiper.chat.firebase.FireBaseServiceImpl
import com.shkiper.chat.interfaces.IFireBaseService
import com.shkiper.chat.repositories.MainRepository
import dagger.Binds
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton


@Module(/*includes = [NetworkModule.NetWorkBinds::class]*/)
@InstallIn(ViewModelComponent::class)
object NetworkModule {


    @Provides
    fun providesFireBaseService(): IFireBaseService = FireBaseServiceImpl()

    @Provides
    fun providesMainRepository(firebaseService: IFireBaseService): MainRepository {
        return MainRepository(firebaseService)
    }


//    @Module
//    @InstallIn(ViewModelComponent::class)
//    abstract class NetWorkBinds {

//        @Binds abstract fun bindMainRepository(firebaseService: FireBaseServiceImpl): IFireBaseService

//    }


}