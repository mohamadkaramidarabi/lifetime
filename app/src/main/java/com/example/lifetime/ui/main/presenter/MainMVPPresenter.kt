package com.example.lifetime.ui.main.presenter

import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.main.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.view.MainMVPView

interface MainMVPPresenter<V: MainMVPView,I: MainMVPInteractor> : MVPPresenter<V,I>{
    fun onButtonClicked()
    fun getPersons(): Boolean?
}