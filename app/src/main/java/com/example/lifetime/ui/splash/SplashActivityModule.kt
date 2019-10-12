package com.example.lifetime.ui.splash

import dagger.Module
import dagger.Provides


@Module
class SplashActivityModule {


    @Provides
    internal fun provideSplashPresenter(splashPresenter: SplashPresenter<SplashInteractor.SplashMVPView>)
            : SplashInteractor.SplashMVPPresenter<SplashInteractor.SplashMVPView> = splashPresenter
}