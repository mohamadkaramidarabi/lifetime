package com.example.lifetime.di.builder

import com.example.lifetime.ui.addperson.view.AddPersonDialog
import com.example.lifetime.ui.main.MainActivityModule
import com.example.lifetime.ui.main.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity
}