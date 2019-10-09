package com.example.lifetime.data.database.repository.life_expectancies

import io.reactivex.Observable
import javax.inject.Inject

class LifeExpectancyRepository @Inject constructor(private val lifeExpectanciesDao: LifeExpectanciesDao):
    LifeExpectancyRepo {

    override fun isLifeExpectancyRepoEmpty(): Observable<Boolean> =
        Observable.fromCallable {
            lifeExpectanciesDao.loadAll().isEmpty()
        }

    override fun insertAll(lifeExpectancies: List<LifeExpectancy>): Observable<Boolean> =
        Observable.fromCallable {
            lifeExpectanciesDao.insertAll(lifeExpectancies)
            true
        }

    override fun getLifeExpectancies(): Observable<List<LifeExpectancy>> =
        Observable.fromCallable {
            lifeExpectanciesDao.loadAll()
        }
}