package com.envyglit.home.di.module

import com.envyglit.core.data.local.Database
import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.home.domain.HomeInteractor
import com.envyglit.home.domain.HomeInteractorImpl
import com.envyglit.home.domain.HomeRepository
import com.envyglit.home.util.mock.HomeMockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun providesHomeRepository(firebaseService: FireBaseService, database: Database): HomeRepository {
        return HomeMockRepository()
    }

    @Provides
    fun providesHomeInteractor(repository: HomeRepository): HomeInteractor {
        return HomeInteractorImpl(repository)
    }

}