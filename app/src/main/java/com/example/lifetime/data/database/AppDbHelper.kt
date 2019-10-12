package com.example.lifetime.data.database

import android.content.Context
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(context: Context): DbHelper {

    private var database: AppDatabase

    init {
        database = AppDatabase.getInstance(context)
    }

    override fun insertPerson(person: Person) = database.personDao().insertPerson(person)

    override fun loadPersons(): List<Person> = database.personDao().loadAll()

    override fun isLifeExpectancyRepoEmpty() = database.lifeExpectanciesDao().loadAll().isEmpty()

    override fun saveLifeExpectancyList(lifeExpectancies: List<LifeExpectancy>) =
        database.lifeExpectanciesDao().insertAll(lifeExpectancies)

    override fun getLifeExpectancyList(): List<LifeExpectancy> =
        database.lifeExpectanciesDao().loadAll()
}