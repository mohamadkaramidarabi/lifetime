package com.example.lifetime.ui.login

import com.example.lifetime.data.AppDataManager
import com.example.lifetime.data.database.repository.person.Person
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
    override fun onDatePickerClicked() {
        getView()?.openDatePickerDialog()
    }

    override fun addMainPersonToDb(person: Person) {
        compositeDisposable.add(
            dataManager.insertPerson(person)
                .compose(schedulerProvider.ioToMainObservableScheduler())
                .subscribe()
        )
    }

    override fun setLoggedInState() {
        dataManager.setCurrentUserLoggedInMode(LoggedInMode.LOGGED_IN_MODE_LOGGED_IN)
    }

    override fun loadLifeExpectancies(): Boolean =
        compositeDisposable.add(dataManager.getLifeExpectancyList()
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe {
                getView()?.getLifeExpectancies(it)
            })


    override fun onRegisterButtonClicked() {
        if (getView()?.checkFormInfo()!!) {
            getView()?.createPersonFromForm().let {person ->
                addMainPersonToDb(person!!)
                setLoggedInState()
            }
            getView()?.openMainActivity()
        }
    }
}