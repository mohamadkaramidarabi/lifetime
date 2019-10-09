package com.example.lifetime.ui.splash.interactor

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.ui.base.interactor.MVPInteractor
import io.reactivex.Observable

interface SplashMVPInteractor : MVPInteractor {
    fun seedLifeExpectancies(): Observable<Boolean>
    fun getLifeExpectancies(): Observable<List<LifeExpectancy>>
    fun uploadLifeExpectanciesDataToDB(lifeExpectancies: List<LifeExpectancy>): Observable<Boolean>
}