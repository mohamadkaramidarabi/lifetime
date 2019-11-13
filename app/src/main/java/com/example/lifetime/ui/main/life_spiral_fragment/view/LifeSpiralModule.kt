package com.example.lifetime.ui.main.life_spiral_fragment.view

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class LifeSpiralModule {
    @Provides
    internal fun providePresenter(presenter: LifeSpiralPresenter<LifeSpiralInteractor.LifeSpiralMVPView>) :
            LifeSpiralInteractor.LifeSpiralMVPPresenter<LifeSpiralInteractor.LifeSpiralMVPView> = presenter

}