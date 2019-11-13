package com.example.lifetime.ui.splash

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView
import io.reactivex.Observable

object SplashInteractor {

    interface SplashMVPView : MVPView {
        fun openMainActivity()
        fun openLoginActivity()
    }

    interface SplashMVPPresenter<V : SplashMVPView> : MVPPresenter<V> {
        fun seedLifeExpectancies(): Boolean
        fun decideActivityToOpen()
    }
}