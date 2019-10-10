package com.example.lifetime.ui.addperson.presenter

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.addperson.interactor.AddPersonMVPInteractor
import com.example.lifetime.ui.addperson.view.AddPersonMVPDialog
import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import javax.inject.Inject

class AddPersonPresenter<V : AddPersonMVPDialog, I : AddPersonMVPInteractor> @Inject internal constructor(
    interactor: I,
    compositeDisposable: CompositeDisposable,
    schedulerProvider: SchedulerProvider
) : BasePresenter<V, I>(interactor,compositeDisposable,schedulerProvider), AddPersonMVPPresenter<V, I> {


    override fun onSubmitButtonClicked(person: Person): Boolean? =
        interactor?.let {
            compositeDisposable.add(
                it.addPersonToDB(person)
                    .compose(schedulerProvider.ioToMainObservableScheduler())
                    .subscribe()
            )
        }

    override fun onDatePickerClicked() {
        getView()?.openDataPickerView()
    }

    override fun onDateSelected(persianCalendar: PersianCalendar) =
        getView()?.onDateSelected(persianCalendar)!!


    override fun requestLifeExpectanciesFromDB(): Boolean =
        compositeDisposable.add(
            interactor!!.loadAllCountries()
                .compose(schedulerProvider.ioToMainObservableScheduler())
                .subscribe {
                    getView()?.getLifeExpectancies(it)
                })




}