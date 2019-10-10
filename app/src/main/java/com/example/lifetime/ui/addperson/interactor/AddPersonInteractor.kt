package com.example.lifetime.ui.addperson.interactor

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancyRepo
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.data.database.repository.person.PersonRepo
import com.example.lifetime.ui.base.interactor.MVPInteractor
import io.reactivex.Observable
import javax.inject.Inject

class AddPersonInteractor @Inject constructor(private val personRepoHelper: PersonRepo,
                                              private val lifeExpectancyRepoHelper: LifeExpectancyRepo): MVPInteractor, AddPersonMVPInteractor {

    override fun addPersonToDB(person: Person): Observable<Boolean> =
        personRepoHelper.insertPerson(person)

    override fun loadAllCountries(): Observable<List<LifeExpectancy>> =
        lifeExpectancyRepoHelper.getLifeExpectancies()

}