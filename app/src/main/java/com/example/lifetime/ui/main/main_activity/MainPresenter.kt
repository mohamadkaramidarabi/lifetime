package com.example.lifetime.ui.main.main_activity

import android.util.Log
import com.example.lifetime.data.AppDataManager
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainPresenter<V : MainInteractor.MainMVPView> @Inject internal constructor(
    dataManager: AppDataManager,
    compositeDisposable: CompositeDisposable,
    schedulerProvider: SchedulerProvider
) : BasePresenter<V>(dataManager, compositeDisposable, schedulerProvider),
    MainInteractor.MainMVPPresenter<V> {

    override fun getPersons(): Boolean =
        compositeDisposable.add(dataManager.loadPersons()
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe {
                Log.d("persons", it.toString())
                getView()?.loadPersons(it.toMutableList())
            })


    override fun onButtonClicked() {
        getView()?.openUserDialog()
    }
}