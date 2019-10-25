package com.example.lifetime.ui.login

import com.example.lifetime.data.AppDataManager
import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.util.LoggedInMode
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginPresenter<V: LoginInteractor.LoginMVPView> @Inject constructor(
    compositeDisposable: CompositeDisposable,
    dataManager: AppDataManager,
    schedulerProvider: SchedulerProvider) : LoginInteractor.LoginMVPPresenter<V>,
BasePresenter<V>(dataManager, compositeDisposable,schedulerProvider)
{
    override fun setLoggedInState() {
        dataManager.setCurrentUserLoggedInMode(LoggedInMode.LOGGED_IN_MODE_LOGGED_IN)
    }

    override fun loadAllCountries(): Boolean =
        compositeDisposable.add(dataManager.getLifeExpectancyList()
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe {
                getView()?.getAllCountries(it.map { lifeExpectancy ->
                    lifeExpectancy.country
                })
            })


}