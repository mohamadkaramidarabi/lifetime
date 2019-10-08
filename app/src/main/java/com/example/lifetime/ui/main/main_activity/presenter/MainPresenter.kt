package com.example.lifetime.ui.main.main_activity.presenter

import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.ui.main.main_activity.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.main_activity.view.MainMVPView
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainPresenter<V : MainMVPView, I : MainMVPInteractor> @Inject internal constructor(
    interactor: I?,
    compositeDisposable: CompositeDisposable,
    schedulerProvider: SchedulerProvider
) : BasePresenter<V, I>(interactor,compositeDisposable,schedulerProvider), MainMVPPresenter<V, I> {

    override fun onButtonClicked() {
        getView()?.openUserDialog()
    }


    override fun getPersons(): Boolean? =
        interactor?.let {
            compositeDisposable.add(it.getPersons()
                .compose(schedulerProvider.ioToMainObservableScheduler())
                .subscribe { persons ->
                    getView()?.loadPersons(persons.toMutableList())
                }
            )
        }


}