package com.example.lifetime.ui.main.presenter

import com.example.lifetime.ui.main.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.view.MainMVPView
import javax.inject.Inject

class MainPresenter<V: MainMVPView, I: MainMVPInteractor> @Inject internal constructor()
    : MainMVPPresenter<V,I> {

    private var view: V? =null
    override fun onAttach(view: V) {
        this.view = view
    }

    override fun onDetach() {
        view = null
    }

    override fun getView(): V? = view

    override fun onButtonClicked() {
        view?.openUserDialog()
    }
}