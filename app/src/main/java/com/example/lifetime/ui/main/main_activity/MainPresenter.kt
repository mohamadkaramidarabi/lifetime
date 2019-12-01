package com.example.lifetime.ui.main.main_activity

import android.graphics.Path
import android.graphics.Point
import com.example.lifetime.data.AppDataManager
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject
import kotlin.math.*

class MainPresenter<V : MainInteractor.MainMVPView> @Inject internal constructor(
    dataManager: AppDataManager,
    compositeDisposable: CompositeDisposable,
    schedulerProvider: SchedulerProvider
) : BasePresenter<V>(dataManager, compositeDisposable, schedulerProvider),
    MainInteractor.MainMVPPresenter<V> {


    override fun deletePerson(person: Person):Boolean{
        if (dataManager.getLastPerson()?.id == person.id) {
            compositeDisposable.add(
                dataManager.loadPersons()
                    .compose(schedulerProvider.ioToMainObservableScheduler())
                    .subscribe {
                        it.map {person ->
                            if (person.isMainUser) {
                                dataManager.setLastPerson(person)
                                getView()?.updateViewAfterDeleteCurrentPerson(person)
                            }
                        }
                    }
            )
        }
        return compositeDisposable.add(
            dataManager.deletePerson(person)
                .compose(schedulerProvider.ioToMainObservableScheduler())
                .subscribe{
                    getView()?.deletePersonFromList(person)
                })
    }

    override fun getPersons(): Boolean =
        compositeDisposable.add(dataManager.loadPersons()
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe {
                getView()?.loadPersons(it.toMutableList())
            })

    override fun getLastPersonFromDb(){
        if (dataManager.getLastPerson() == null) {
            compositeDisposable.add(dataManager.loadPersons()
                .compose(schedulerProvider.ioToMainObservableScheduler())
                .subscribe {
                    it.map { person ->
                        if (person.isMainUser) getView()?.getLastPerson(person)
                    }
                })
        } else {
            getView()?.getLastPerson(dataManager.getLastPerson()!!)
        }

    }

    override fun onButtonClicked() {
        getView()?.openUserDialog(null)
    }

    override fun onPersonClicked(person: Person) {
        getView()?.getPersonFromList(person)
    }

    override fun setLastPersonOnDb(person: Person) {
        dataManager.setLastPerson(person)
    }


}