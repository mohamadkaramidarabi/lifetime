package info.vazeh.android.data.preferences


import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.util.LoggedInMode


interface PreferenceHelper {
    fun getCurrentUserLoggedInMode(): Int
    fun setCurrentUserLoggedInMode(mode: LoggedInMode)
    fun setLastPerson(person: Person)
    fun getLastPerson(): Person?
}