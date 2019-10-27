package com.example.lifetime.ui.main.life_spiral_fragment.view

import com.example.lifetime.data.AppDataManager
import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LifeSpiralPresenter<V : LifeSpiralInteractor.LifeSpiralMVPView> @Inject constructor(compositeDisposable: CompositeDisposable,
                                                                                          dataManager: AppDataManager,
                                                                                          schedulerProvider: SchedulerProvider)
    :BasePresenter<V>(dataManager,compositeDisposable,schedulerProvider),
    LifeSpiralInteractor.LifeSpiralMVPPresenter<V> {


    override fun getMainPersonFromDataBase(): Boolean = compositeDisposable.add(
        dataManager.loadPersons().compose(schedulerProvider.ioToMainObservableScheduler()).doOnNext { personList ->
            personList.filter { person ->
                person.isMainUser
            }.map {
                getView()?.getMainPerson(it)
            }
        }.subscribe()
    )


}