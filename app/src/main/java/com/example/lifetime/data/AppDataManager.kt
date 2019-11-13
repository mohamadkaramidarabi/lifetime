package com.example.lifetime.data

import com.example.lifetime.data.database.DbHelper
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.util.LoggedInMode
import info.vazeh.android.data.preferences.PreferenceHelper
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(private val dbHelper: DbHelper,
                                         private val preferenceHelper: PreferenceHelper): DataManager {
    override fun insertPerson(person: Person) =
        dbHelper.insertPerson(person)

    override fun updatePerson(person: Person): Observable<Int> =
        dbHelper.updatePerson(person)

    override fun loadPersons() =
        dbHelper.loadPersons()

    override fun deletePerson(person: Person): Observable<Unit> =
        dbHelper.deletePerson(person)

    override fun isLifeExpectancyRepoEmpty() =
        dbHelper.isLifeExpectancyRepoEmpty()

    override fun saveLifeExpectancyList(lifeExpectancies: List<LifeExpectancy>)  =
        dbHelper.saveLifeExpectancyList(lifeExpectancies)

    override fun getLifeExpectancyList()  =
        dbHelper.getLifeExpectancyList()

    override fun getCurrentUserLoggedInMode(): Int =
        preferenceHelper.getCurrentUserLoggedInMode()

    override fun setCurrentUserLoggedInMode(mode: LoggedInMode) =
        preferenceHelper.setCurrentUserLoggedInMode(mode)

    override fun setLastPerson(person: Person) =
        preferenceHelper.setLastPerson(person)

    override fun getLastPerson(): Person? =
        preferenceHelper.getLastPerson()

}