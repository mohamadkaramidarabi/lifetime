package com.example.lifetime.ui.main.main_activity.view

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.MVPView

interface MainMVPView : MVPView{

    fun openUserDialog()
    fun loadPersons(persons: MutableList<Person>)

}