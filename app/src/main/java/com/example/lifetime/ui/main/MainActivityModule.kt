package com.example.lifetime.ui.main

import com.example.lifetime.ui.main.main_activity.interactor.MainInteractor
import com.example.lifetime.ui.main.main_activity.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.main_activity.presenter.MainMVPPresenter
import com.example.lifetime.ui.main.main_activity.presenter.MainPresenter
import com.example.lifetime.ui.main.main_activity.view.MainMVPView
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    internal fun providePresenter(presenter: MainPresenter<MainMVPView, MainMVPInteractor>)
            : MainMVPPresenter<MainMVPView, MainMVPInteractor> = presenter

    @Provides
    internal fun provideInteractor(interactor: MainInteractor): MainMVPInteractor = interactor

}