package com.example.lifetime.ui.addperson.interactor

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.interactor.MVPInteractor
import io.reactivex.Observable

interface AddPersonMVPInteractor : MVPInteractor {
    fun addPersonToDB(person: Person): Observable<Boolean>
}