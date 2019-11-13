package com.example.lifetime.ui.base.presenter

import com.example.lifetime.data.AppDataManager
import com.example.lifetime.ui.base.view.MVPView
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V : MVPView> internal constructor(
    protected val dataManager: AppDataManager,
    protected val compositeDisposable: CompositeDisposable,
    protected val schedulerProvider: SchedulerProvider
) : MVPPresenter<V> {

    private var view: V? = null
    override fun getView(): V? = view

    override fun onAttach(view: V?) {
        this.view = view
    }

    override fun onDetach() {
        this.view=null
    }
}