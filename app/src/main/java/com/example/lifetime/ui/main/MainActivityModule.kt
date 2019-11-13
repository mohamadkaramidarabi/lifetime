package com.example.lifetime.ui.main

import com.example.lifetime.ui.main.main_activity.MainInteractor
import com.example.lifetime.ui.main.main_activity.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    @MainScop
    internal fun providePresenter(presenter: MainPresenter<MainInteractor.MainMVPView>)
            : MainInteractor.MainMVPPresenter<MainInteractor.MainMVPView> = presenter

}