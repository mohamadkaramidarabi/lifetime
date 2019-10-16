package com.example.lifetime.data

import com.example.lifetime.data.database.DbHelper
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.util.LoggedInMode
import info.vazeh.android.data.preferences.PreferenceHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject constructor(private val dbHelper: DbHelper,
                                         private val preferenceHelper: PreferenceHelper): DataManager {
    override fun insertPerson(person: Person) =
        dbHelper.insertPerson(person)

    override fun loadPersons() =
        dbHelper.loadPersons()

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

    override fun getAuthorization(): String =
        preferenceHelper.getAuthorization()

    override fun setAuthorization(auth: String) =
        preferenceHelper.setAuthorization(auth)

    override fun setIsCopiedDatabase(isCopied: Boolean) =
        preferenceHelper.setIsCopiedDatabase(isCopied)

    override fun isCopiedDatabase(): Boolean  =
        preferenceHelper.isCopiedDatabase()

    override fun setTranslationMode(isTranslationMode: Boolean) =
        preferenceHelper.setTranslationMode(isTranslationMode)

    override fun isTranslationMode(): Boolean =
        preferenceHelper.isTranslationMode()

    override fun setIsSelectedLang(value: Boolean) =
        preferenceHelper.setIsSelectedLang(value)

    override fun isSelectedLang(): Boolean  =
        preferenceHelper.isSelectedLang()

}