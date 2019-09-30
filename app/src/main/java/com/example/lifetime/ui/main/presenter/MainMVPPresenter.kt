package com.example.lifetime.ui.main.presenter

import com.example.lifetime.ui.main.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.view.MainMVPView

interface MainMVPPresenter<V: MainMVPView,I: MainMVPInteractor>{
    fun onButtonClicked()
    fun onAttach(view: V)
    fun onDetach()
    fun getView(): V?
}