package com.generals.zimmerfrei.inject

import android.content.Context
import com.generals.zimmerfrei.ZimmerFreiApplication
import com.generals.zimmerfrei.navigator.Navigator
import com.generals.zimmerfrei.navigator.NavigatorImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: ZimmerFreiApplication): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideNavigator(navigator: NavigatorImpl): Navigator = navigator
}