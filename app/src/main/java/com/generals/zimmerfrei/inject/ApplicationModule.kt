package com.generals.zimmerfrei.inject

import android.content.Context
import com.generals.zimmerfrei.ZimmerFreiApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: ZimmerFreiApplication): Context = application.applicationContext
}