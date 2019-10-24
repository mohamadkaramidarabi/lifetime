package com.example.lifetime.ui.main.main_activity

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView

object MainInteractor {

    interface MainMVPView : MVPView {
        fun openUserDialog(person: Person?=null)
        fun loadPersons(persons: MutableList<Person>)
        fun deletePersonFromList(person: Person)
    }

    interface MainMVPPresenter<V : MainMVPView> : MVPPresenter<V> {
        fun onButtonClicked()
        fun getPersons(): Boolean
        fun deletePerson(person: Person): Boolean

    }
}