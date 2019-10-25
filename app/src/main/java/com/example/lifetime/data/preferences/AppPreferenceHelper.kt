package info.vazeh.android.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.lifetime.di.PreferenceInfo
import com.example.lifetime.util.LoggedInMode
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
}


