package com.example.lifetime.di.builder

import com.example.lifetime.ui.main.life_spiral_fragment.view.LifeSpiralFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun bindLifeSpiralFragment(): LifeSpiralFragment
}