package com.example.lifetime.data.database.repository.person

import io.reactivex.Observable
import javax.inject.Inject

class PersonRepository @Inject internal constructor(private val personDao: PersonDao): PersonRepo {
    override fun isPersonRepoEmpty(): Observable<Boolean> =
        Observable.fromCallable {
            personDao.loadAll().isEmpty()
        }

    override fun insertPerson(person: Person): Observable<Boolean>{
        personDao.insertPerson(person)
        return Observable.just(true)
    }


    override fun loadPersons(): Observable<List<Person>> =
        Observable.fromCallable {
            personDao.loadAll()
        }
}