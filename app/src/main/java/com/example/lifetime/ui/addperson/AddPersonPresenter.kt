package com.example.lifetime.ui.addperson

import com.example.lifetime.data.AppDataManager
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import javax.inject.Inject

class AddPersonPresenter<V : AddPersonInteractor.AddPersonMVPDialog> @Inject internal constructor(
    dataManager: AppDataManager,
    compositeDisposable: CompositeDisposable,
    schedulerProvider: SchedulerProvider
) : BasePresenter<V>(dataManager,compositeDisposable,schedulerProvider),
    AddPersonInteractor.AddPersonMVPPresenter<V> {


    override fun addPersonToDB(person: Person) =
        dataManager.insertPerson(person)

    override fun loadAllCountries(): List<LifeExpectancy> =
        dataManager.getLifeExpectancyList()


    override fun onSubmitButtonClicked(person: Person) =
        addPersonToDB(person)

    override fun onDatePickerClicked() {
        getView()?.openDataPickerView()
    }

    override fun onDateSelected(persianCalendar: PersianCalendar) =
        getView()?.onDateSelected(persianCalendar)!!

}