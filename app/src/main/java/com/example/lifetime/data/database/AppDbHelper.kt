package com.example.lifetime.data.database

import android.content.Context
import android.util.Log
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject
constructor(context: Context): DbHelper {

    private var database: AppDatabase

    init {
        database = AppDatabase.getInstance(context)
    }

    override fun insertPerson(person: Person): Observable<Unit> =
        Observable.fromCallable { database.personDao().insertPerson(person) }

    override fun updatePerson(person: Person): Observable<Int> =
        Observable.fromCallable {
            database.personDao().updatePerson(person)
        }


    override fun loadPersons()= Observable.fromCallable { database.personDao().loadAll() }


    override fun deletePerson(person: Person): Observable<Unit> =
        Observable.fromCallable { database.personDao().deletePerson(person) }

    override fun isLifeExpectancyRepoEmpty(): Observable<Boolean> =
        Observable.just(database.lifeExpectanciesDao().loadAll().isEmpty())

    override fun saveLifeExpectancyList(lifeExpectancies: List<LifeExpectancy>): Observable<Boolean> {
        database.lifeExpectanciesDao().insertAll(lifeExpectancies)
        return Observable.just(true)
    }

    override fun getLifeExpectancyList(): Observable<List<LifeExpectancy>> =
        Observable.fromCallable { database.lifeExpectanciesDao().loadAll() }
}