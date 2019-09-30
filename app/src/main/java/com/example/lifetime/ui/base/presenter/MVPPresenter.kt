package com.example.lifetime.ui.base.presenter

import com.example.lifetime.ui.base.interactor.MVPInteractor
import com.example.lifetime.ui.base.view.MVPView

interface MVPPresenter<V : MVPView, I : MVPInteractor> {
    fun onAttach(view: V?)
    fun ondetach()
    fun getView(): V?
}
