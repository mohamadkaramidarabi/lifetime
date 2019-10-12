package com.example.lifetime.data.database

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import io.reactivex.Observable

interface DbHelper {
    fun insertPerson(person: Person)
    fun loadPersons(): List<Person>

    fun isLifeExpectancyRepoEmpty(): Boolean
    fun saveLifeExpectancyList(lifeExpectancies: List<LifeExpectancy>)
    fun getLifeExpectancyList(): List<LifeExpectancy>

}