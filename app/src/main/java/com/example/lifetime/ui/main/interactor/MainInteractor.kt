package com.example.lifetime.ui.main.interactor

import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.data.database.repository.person.PersonRepo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainInteractor @Inject internal constructor(private val personRepo: PersonRepo): MainMVPInteractor {

    override fun getPersons(): Observable<List<Person>> = personRepo.loadPersons()

}