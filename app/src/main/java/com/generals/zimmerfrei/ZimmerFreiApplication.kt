package com.generals.zimmerfrei

import android.app.Activity
import android.app.Application
import com.crashlytics.android.Crashlytics
import com.generals.zimmerfrei.inject.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import javax.inject.Inject

class ZimmerFreiApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
            .create(this)
            .inject(this)

        Fabric.with(this, Crashlytics())
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector
}