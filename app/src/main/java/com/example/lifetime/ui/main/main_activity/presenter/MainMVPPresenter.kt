package com.example.lifetime.ui.main.main_activity.presenter

import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.main.main_activity.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.main_activity.view.MainMVPView

interface MainMVPPresenter<V: MainMVPView,I: MainMVPInteractor> : MVPPresenter<V,I>{
    fun onButtonClicked()
    fun getPersons(): Boolean?
}