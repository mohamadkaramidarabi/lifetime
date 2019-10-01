package com.example.lifetime.data.database.repository.person

import io.reactivex.Observable

interface PersonRepo {
    fun isPersonRepoEmpty(): Observable<Boolean>
    fun insertPerson(person: Person): Observable<Boolean>
    fun loadPersons(): Observable<List<Person>>
}