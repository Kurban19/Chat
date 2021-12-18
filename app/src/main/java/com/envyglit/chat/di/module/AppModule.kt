package com.envyglit.chat.di.module

import com.envyglit.chat.domain.repository.Repository
import com.envyglit.chat.util.mock.HomeMockRepository
import com.envyglit.chat.util.mock.MockRepository
import com.envyglit.core.data.local.Database
import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.home.domain.HomeInteractor
import com.envyglit.home.domain.HomeInteractorImpl
import com.envyglit.home.domain.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, RoomModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesMainRepository(firebaseService: FireBaseService, database: Database): Repository {
        return MockRepository()
    }

    @Provides
    fun providesHomeRepository(firebaseService: FireBaseService, database: Database): HomeRepository {
        return HomeMockRepository()
    }

    @Provides
    fun providesHomeInteractor(repository: HomeRepository): HomeInteractor {
        return HomeInteractorImpl(repository) //TODO move to feature-home
    }

}
