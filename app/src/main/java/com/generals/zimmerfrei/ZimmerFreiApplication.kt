package com.generals.zimmerfrei

import android.app.Application
import android.content.Context
import com.generals.zimmerfrei.inject.ApplicationComponent
import com.generals.zimmerfrei.inject.ApplicationModule
import com.generals.zimmerfrei.inject.DaggerApplicationComponent

class ZimmerFreiApplication : Application() {

    lateinit var component: ApplicationComponent

    companion object {

        fun applicationComponent(context: Context): ApplicationComponent = (context.applicationContext as ZimmerFreiApplication).component
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this)).build()
    }
}