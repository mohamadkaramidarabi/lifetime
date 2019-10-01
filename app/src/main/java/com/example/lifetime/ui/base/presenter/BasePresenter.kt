package com.example.lifetime.ui.base.presenter

import com.example.lifetime.ui.base.interactor.MVPInteractor
import com.example.lifetime.ui.base.view.MVPView

abstract class BasePresenter<V : MVPView, I : MVPInteractor> internal constructor(
    protected var interactor: I?) : MVPPresenter<V, I> {

    private var view: V? = null
    override fun getView(): V? = view

    override fun onAttach(view: V?) {
        this.view = view
    }

    override fun onDetach() {
        this.view=null
        interactor = null

    }



}