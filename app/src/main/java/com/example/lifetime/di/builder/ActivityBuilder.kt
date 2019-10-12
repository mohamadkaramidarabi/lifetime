package com.example.lifetime.di.builder

import com.example.lifetime.ui.main.MainActivityModule
import com.example.lifetime.ui.main.main_activity.MainActivity
import com.example.lifetime.ui.splash.SplashActivityModule
import com.example.lifetime.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class,
        DialogBuilder::class,
        FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    abstract fun bindSplashActivity(): SplashActivity
}