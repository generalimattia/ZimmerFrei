package com.generals.zimmerfrei

import com.generals.zimmerfrei.inject.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class ZimmerFreiApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerApplicationComponent.builder()
        .create(this)
}