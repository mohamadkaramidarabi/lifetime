package com.example.lifetime.ui.main.interactor

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.interactor.MVPInteractor
import io.reactivex.Observable

interface MainMVPInteractor : MVPInteractor {

    fun getPersons(): Observable<List<Person>>
}