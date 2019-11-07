package com.example.lifetime.util

import com.example.lifetime.BuildConfig
import timber.log.Timber

object AppLogger {

    @JvmStatic
    fun d(s: String, vararg objects: Any) {
        Timber.d(s, *objects)
    }

    @JvmStatic
    fun d(throwable: Throwable, s: String, vararg objects: Any) {
        Timber.d(throwable, s, *objects)
    }

    @JvmStatic
    fun e(s: String, vararg objects: Any) {
        Timber.e(s, *objects)
    }

    @JvmStatic
    fun e(throwable: Throwable, s: String, vararg objects: Any) {
        Timber.e(throwable, s, *objects)
    }

    @JvmStatic
    fun i(s: String, vararg objects: Any) {
        Timber.i(s, *objects)
    }

    @JvmStatic
    fun i(throwable: Throwable, s: String, vararg objects: Any) {
        Timber.i(throwable, s, *objects)
    }

    @JvmStatic
    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    @JvmStatic fun w(s: String, vararg objects: Any) {
        Timber.w(s, *objects)
    }

    @JvmStatic fun w(throwable: Throwable, s: String, vararg objects: Any) {
        Timber.w(throwable, s, *objects)
    }
}