package com.example.lifetime.ui.login

import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView

object LoginInteractor {

    interface LoginMVPView: MVPView {
        fun openMainActivity()
        fun onRegisterButtonClicked()
        fun getAllCountries(countries: List<String>)
    }

    interface LoginMVPPresenter<V: LoginMVPView>: MVPPresenter<V>  {
        fun setLoggedInState()
        fun loadAllCountries(): Boolean
    }

}