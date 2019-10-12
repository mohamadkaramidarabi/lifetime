package com.example.lifetime.ui.splash

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView

object SplashInteractor {

    interface SplashMVPView : MVPView {
        fun openMainActivity()
    }

    interface SplashMVPPresenter<V : SplashMVPView> : MVPPresenter<V> {
        fun seedLifeExpectancies(): Boolean
        fun getLifeExpectancies(): List<LifeExpectancy>
    }
}