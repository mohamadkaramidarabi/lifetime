package com.example.lifetime.ui.addperson.interactor

import android.util.Log
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.data.database.repository.person.PersonRepo
import com.example.lifetime.ui.base.interactor.MVPInteractor
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddPersonInteractor @Inject constructor(private val personRepoHelper: PersonRepo): MVPInteractor, AddPersonMVPInteractor {

    override fun addPersonToDB(person: Person): Observable<Boolean> =
        personRepoHelper.insertPerson(person)
}