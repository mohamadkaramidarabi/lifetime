package com.example.lifetime.data.database.repository.life_expectancies

import io.reactivex.Observable

interface LifeExpectancyRepo {
    fun isLifeExpectancyRepoEmpty(): Observable<Boolean>
    fun insertAll(lifeExpectancies: List<LifeExpectancy>): Observable<Boolean>
    fun getLifeExpectancies(): Observable<List<LifeExpectancy>>
}