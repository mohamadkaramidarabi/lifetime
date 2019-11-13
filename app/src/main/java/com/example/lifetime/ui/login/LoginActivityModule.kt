package com.example.lifetime.ui.login

import dagger.Module
import dagger.Provides

@Module
class LoginActivityModule {

    @Provides
    internal fun providePresenter(presenter: LoginPresenter<LoginInteractor.LoginMVPView>)
            : LoginInteractor.LoginMVPPresenter<LoginInteractor.LoginMVPView> = presenter

}