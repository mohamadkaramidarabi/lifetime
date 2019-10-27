package com.example.lifetime.di.builder

import com.example.lifetime.ui.about.AboutFragment
import com.example.lifetime.ui.main.life_spiral_fragment.view.LifeSpiralFragment
import com.example.lifetime.ui.main.life_spiral_fragment.view.LifeSpiralModule
import com.example.lifetime.ui.message.MessageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = [LifeSpiralModule::class])
    abstract fun bindLifeSpiralFragment(): LifeSpiralFragment

    @ContributesAndroidInjector
    abstract fun bindMessageFragment(): MessageFragment

    @ContributesAndroidInjector
    abstract fun bindAboutFragment(): AboutFragment
}