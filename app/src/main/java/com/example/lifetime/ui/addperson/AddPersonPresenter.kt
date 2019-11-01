package com.example.lifetime.ui.addperson

import com.example.lifetime.data.AppDataManager
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


    override fun addPersonToDB(person: Person): Boolean =
        compositeDisposable.add(dataManager.insertPerson(person)
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe()
        )

    override fun updatePersonInDB(person: Person): Boolean {
        if (dataManager.getLastPerson()?.id == person.id) {
            dataManager.setLastPerson(person)
        }
        return compositeDisposable.add(dataManager.updatePerson(person)
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe()
        )
    }


    override fun loadAllCountries(): Boolean =
        compositeDisposable.add(dataManager.getLifeExpectancyList()
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe {
                getView()?.getLifeExpectancies(it)
            })


    override fun onSubmitButtonClicked(person: Person) =
        if (getView()!!.isForUpdate()) updatePersonInDB(person) else addPersonToDB(person)


    override fun onDatePickerClicked() {
        getView()?.openDataPickerView()
    }

    override fun onDateSelected(persianCalendar: PersianCalendar) =
        getView()?.onDateSelected(persianCalendar)!!

}

