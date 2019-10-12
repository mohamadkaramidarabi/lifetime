package com.example.lifetime.ui.base.presenter

import com.example.lifetime.ui.base.interactor.MVPInteractor
import com.example.lifetime.ui.base.view.MVPView

interface MVPPresenter<V : MVPView> {
    fun getView(): V?
    fun onAttach(view: V?)
    fun onDetach()
}
