package com.example.lifetime.di.builder

import com.example.lifetime.ui.main.MainActivityModule
import com.example.lifetime.ui.main.main_activity.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class,
        DialogBuilder::class,
        FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity
}