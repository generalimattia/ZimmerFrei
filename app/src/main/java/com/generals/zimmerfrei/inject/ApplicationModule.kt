package com.generals.zimmerfrei.inject

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(context: Context) {

    private val context: Context = context.applicationContext

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }
}