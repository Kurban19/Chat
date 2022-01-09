package com.envyglit.users.di.component

import com.envyglit.users.di.module.UsersModule
import dagger.Component

@Component(modules = [UsersModule::class])
interface UsersComponent
