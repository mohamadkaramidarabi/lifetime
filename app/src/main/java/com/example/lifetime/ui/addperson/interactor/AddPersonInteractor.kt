package com.example.lifetime.ui.addperson.interactor

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.data.database.repository.person.PersonRepo
import com.example.lifetime.ui.base.interactor.MVPInteractor
import io.reactivex.Observable
import javax.inject.Inject

class AddPersonInteractor @Inject constructor(private val personRepoHelper: PersonRepo): MVPInteractor, AddPersonMVPInteractor {

    override fun addPersonToDB(person: Person): Observable<Boolean> =
        personRepoHelper.insertPerson(person)
}