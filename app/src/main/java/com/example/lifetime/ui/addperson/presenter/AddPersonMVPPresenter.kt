package com.example.lifetime.ui.addperson.presenter

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.addperson.interactor.AddPersonMVPInteractor
import com.example.lifetime.ui.addperson.view.AddPersonMVPDialog
import com.example.lifetime.ui.base.presenter.MVPPresenter

interface AddPersonMVPPresenter<V : AddPersonMVPDialog, I: AddPersonMVPInteractor>: MVPPresenter<V,I> {
    fun onSubmitButtonClicked(person: Person)
    fun onDataPickerClicked()
}