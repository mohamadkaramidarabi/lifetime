package info.vazeh.android.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.di.PreferenceInfo
import com.example.lifetime.util.LoggedInMode
import com.google.gson.ExclusionStrategy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javax.inject.Inject


class AppPreferenceHelper
@Inject constructor(context: Context, @PreferenceInfo private val prefFileName: String) :
    PreferenceHelper {


    companion object {
        const val PREF_KEY_USER_LOGGED_IN_MODE = "KEY_USER_LOGGED_IN_MODE"
    }

    private val mPrefs: SharedPreferences =
        context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override fun getCurrentUserLoggedInMode(): Int =
        mPrefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE, LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.type)


    override fun setCurrentUserLoggedInMode(mode: LoggedInMode) =
        mPrefs.edit().putInt(PREF_KEY_USER_LOGGED_IN_MODE, mode.type).apply()

    override fun setLastPerson(person: Person) {
        mPrefs.edit().putString("last_person", Gson().toJson(person, Person::class.java)).apply()
    }

    override fun getLastPerson(): Person? =
        Gson().fromJson(mPrefs.getString("last_person", null), Person::class.java)
}


