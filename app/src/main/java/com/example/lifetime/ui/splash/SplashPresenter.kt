package com.example.lifetime.ui.splash

import android.content.Context
import com.example.lifetime.data.AppDataManager
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.util.FileUtils
import com.example.lifetime.util.LoggedInMode
import com.example.lifetime.util.SchedulerProvider
import com.google.gson.GsonBuilder
import com.google.gson.internal.`$Gson$Types`
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SplashPresenter<V : SplashInteractor.SplashMVPView>
@Inject internal constructor(
    dataManager: AppDataManager,
    compositeDisposable: CompositeDisposable,
    schedulerProvider: SchedulerProvider
) :
    BasePresenter<V>(dataManager, compositeDisposable, schedulerProvider),
    SplashInteractor.SplashMVPPresenter<V> {

    @Inject lateinit var context: Context



    override fun seedLifeExpectancies(): Boolean =
        compositeDisposable.add(
            dataManager.isLifeExpectancyRepoEmpty()
                .compose(schedulerProvider.ioToMainObservableScheduler())
                .subscribe { isEmpty ->
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
                        dataManager.saveLifeExpectancyList(lifeExpectancies)
                    }
                        decideActivityToOpen()

                }
        )

    override fun decideActivityToOpen() {
        if (isUserLoggedIn()) {
            getView()?.openMainActivity()
        } else {
            getView()?.openLoginActivity()
        }
    }

    private fun isUserLoggedIn() =
        dataManager.getCurrentUserLoggedInMode() == LoggedInMode.LOGGED_IN_MODE_LOGGED_IN.type
}