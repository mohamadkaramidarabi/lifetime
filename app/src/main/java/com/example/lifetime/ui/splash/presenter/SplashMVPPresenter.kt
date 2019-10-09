package com.example.lifetime.ui.splash.presenter

import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.splash.interactor.SplashMVPInteractor
import com.example.lifetime.ui.splash.view.SplashMVPView

interface SplashMVPPresenter<V: SplashMVPView, I: SplashMVPInteractor> : MVPPresenter<V,I>