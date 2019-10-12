package com.example.lifetime.ui.addperson

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView
import ir.hamsaa.persiandatepicker.util.PersianCalendar

object AddPersonInteractor {

    interface AddPersonMVPPresenter<V : AddPersonMVPDialog>: MVPPresenter<V> {
        fun onSubmitButtonClicked(person: Person)
        fun onDatePickerClicked()
        fun onDateSelected(persianCalendar: PersianCalendar)
        fun addPersonToDB(person: Person)
        fun loadAllCountries(): List<LifeExpectancy>
    }

    interface AddPersonMVPDialog : MVPView {
        fun openDataPickerView()
        fun dismissDialog()
        fun onDateSelected(persianCalendar: PersianCalendar)
        fun getLifeExpectancies(lifeExpectancies: List<LifeExpectancy>)
    }
}