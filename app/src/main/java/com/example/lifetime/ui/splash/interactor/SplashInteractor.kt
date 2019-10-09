package com.example.lifetime.ui.splash.interactor

import android.content.Context
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancyRepo
import com.example.lifetime.util.FileUtils
import com.google.gson.GsonBuilder
import com.google.gson.internal.`$Gson$Types`
import io.reactivex.Observable
import javax.inject.Inject

class SplashInteractor @Inject
constructor(private val context: Context,private val lifeExpectancyRepoHelper: LifeExpectancyRepo) : SplashMVPInteractor {

    override fun uploadLifeExpectanciesDataToDB( lifeExpectancies: List<LifeExpectancy>): Observable<Boolean> =
        lifeExpectancyRepoHelper.insertAll(lifeExpectancies)

    override fun seedLifeExpectancies(): Observable<Boolean> =
        lifeExpectancyRepoHelper.isLifeExpectancyRepoEmpty().concatMap { isEmpty ->
            if (isEmpty) {
                val builder = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                val gson = builder.create()
                val type = `$Gson$Types`.newParameterizedTypeWithOwner(
                    null,
                    List::class.java,
                    LifeExpectancy::class.java
                )
                val lifeExpectancies = gson.fromJson<List<LifeExpectancy>>(
                    FileUtils.loadJSONFromAsset(context, "country_by_life_expectancy.json"),
                    type
                )
                lifeExpectancyRepoHelper.insertAll(lifeExpectancies = lifeExpectancies)
            } else {
                Observable.just(false)
            }

        }

    override fun getLifeExpectancies(): Observable<List<LifeExpectancy>> =
        lifeExpectancyRepoHelper.getLifeExpectancies()

}