package com.shkiper.chat.di.component

import dagger.Component
import com.shkiper.chat.di.module.NetworkModule
import com.shkiper.chat.ui.archive.ArchiveActivity
import com.shkiper.chat.ui.chat.ChatActivity
import com.shkiper.chat.ui.main.MainActivity
import com.shkiper.chat.ui.users.UsersActivity
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(activity: UsersActivity)

    fun inject(activity: ChatActivity)

    fun inject(activity: ArchiveActivity)
}