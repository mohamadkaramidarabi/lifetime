package com.example.lifetime.ui.addperson.presenter

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.addperson.interactor.AddPersonMVPInteractor
import com.example.lifetime.ui.addperson.view.AddPersonMVPDialog
import com.example.lifetime.ui.base.presenter.BasePresenter
import javax.inject.Inject

class AddPersonPresenter<V : AddPersonMVPDialog, I : AddPersonMVPInteractor> @Inject internal constructor(
    interactor: I
) : BasePresenter<V, I>(interactor), AddPersonMVPPresenter<V, I> {

    override fun onSubmitButtonClicked(person: Person) {
        interactor?.addPersonToDB(person)
    }
}