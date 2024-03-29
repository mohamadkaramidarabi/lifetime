package com.example.lifetime.data.database

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import io.reactivex.Observable

interface DbHelper {
    fun insertPerson(person: Person): Observable<Unit>
    fun loadPersons(): Observable<List<Person>>
    fun updatePerson(person: Person): Observable<Int>
    fun deletePerson(person: Person): Observable<Unit>

    fun isLifeExpectancyRepoEmpty(): Observable<Boolean>
    fun saveLifeExpectancyList(lifeExpectancies: List<LifeExpectancy>): Observable<Boolean>
    fun getLifeExpectancyList(): Observable<List<LifeExpectancy>>

}