package com.example.lifetime.ui.addperson.presenter

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.addperson.interactor.AddPersonMVPInteractor
import com.example.lifetime.ui.addperson.view.AddPersonMVPDialog
import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.util.SchedulerProvider
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
                    .subscribe {
                        it
                    }
            )
        }

    override fun onDatePickerClicked() {
        getView()?.openDataPickerView()
    }

    override fun onDateSelected(persianCalendar: PersianCalendar) =
        getView()?.onDateSelected(persianCalendar.persianLongDate, calculateAge(persianCalendar))!!


    private fun calculateAge(persianCalendar: PersianCalendar): Int =
        ((PersianCalendar().time.time - persianCalendar.time.time) / (24 * 60 * 60 * 1000)).toInt()

}