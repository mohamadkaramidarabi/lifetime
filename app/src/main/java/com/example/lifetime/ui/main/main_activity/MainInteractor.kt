package com.example.lifetime.ui.main.main_activity

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView

object MainInteractor {

    interface MainMVPView : MVPView {
        fun openUserDialog()
        fun loadPersons(persons: MutableList<Person>)
    }

    interface MainMVPPresenter<V : MainMVPView> : MVPPresenter<V> {
        fun onButtonClicked()
        fun getPersons(): List<Person>
    }
}