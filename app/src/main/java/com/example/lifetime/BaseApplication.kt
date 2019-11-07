package com.example.lifetime

import android.app.Application
import android.content.Context
import com.example.lifetime.di.component.DaggerAppComponent
import com.example.lifetime.util.AppLogger
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
class BaseApplication: DaggerApplication() {

    init {
        application = this
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder()
            .application(this)
            .build()

    companion object {
        private var application: BaseApplication? = null

        @JvmStatic
        fun get(): Context = application!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        AppLogger.init()

    }
}