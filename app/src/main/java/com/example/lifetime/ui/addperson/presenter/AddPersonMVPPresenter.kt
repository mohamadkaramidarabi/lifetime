package com.example.lifetime.ui.addperson.presenter

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.addperson.interactor.AddPersonMVPInteractor
import com.example.lifetime.ui.addperson.view.AddPersonMVPDialog
import com.example.lifetime.ui.base.presenter.MVPPresenter
import ir.hamsaa.persiandatepicker.util.PersianCalendar

interface AddPersonMVPPresenter<V : AddPersonMVPDialog, I: AddPersonMVPInteractor>: MVPPresenter<V,I> {
    fun onSubmitButtonClicked(person: Person): Boolean?
    fun onDatePickerClicked()
    fun onDateSelected(persianCalendar: PersianCalendar)
    fun requestLifeExpectanciesFromDB(): Boolean
}