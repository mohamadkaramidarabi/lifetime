package com.example.lifetime.ui.main

import com.example.lifetime.ui.main.interactor.MainInteractor
import com.example.lifetime.ui.main.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.presenter.MainMVPPresenter
import com.example.lifetime.ui.main.presenter.MainPresenter
import com.example.lifetime.ui.main.view.MainMVPView
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