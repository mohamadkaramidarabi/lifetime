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
        const val TAG = "AppPreferenceHelper"
        const val PREF_KEY_USER_LOGGED_IN_MODE = "KEY_USER_LOGGED_IN_MODE"
        const val PREF_KEY_USER_AUTHORIZATION = "USER_AUTHORIZATION"
        const val PREF_KEY_IS_COPIED_DATABASE = "IS_COPIED_DATABASE"
        const val PREF_KEY_IS_TRANSLATION_MODE = "PREF_KEY_IS_TRANSLATION_MODE"
        const val PREF_KEY_IS_SELECT_LANGUAGE = "PREF_KEY_IS_SELECT_LANGUAGE"
    }

    private val mPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override fun getCurrentUserLoggedInMode(): Int =
            mPrefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE, LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.type)


    override fun setCurrentUserLoggedInMode(mode: LoggedInMode) =
            mPrefs.edit ().
                putInt(PREF_KEY_USER_LOGGED_IN_MODE, mode.type).apply()


    override fun getAuthorization() = mPrefs.getString(PREF_KEY_USER_AUTHORIZATION,"") ?: ""

    override fun setAuthorization(auth: String) {
        mPrefs.edit().putString(PREF_KEY_USER_AUTHORIZATION,auth).apply()
    }

    override fun setIsCopiedDatabase(isCopied: Boolean)
        =mPrefs.edit().putBoolean(PREF_KEY_IS_COPIED_DATABASE,isCopied).apply()

    override fun isCopiedDatabase()
        =mPrefs.getBoolean(PREF_KEY_IS_COPIED_DATABASE,false)

    override fun setTranslationMode(isTranslationMode : Boolean)
            =mPrefs.edit().putBoolean(PREF_KEY_IS_TRANSLATION_MODE,isTranslationMode).apply()

    override fun isTranslationMode()
            =mPrefs.getBoolean(PREF_KEY_IS_TRANSLATION_MODE,false)


    override fun setIsSelectedLang(value: Boolean)
            =mPrefs.edit().putBoolean(PREF_KEY_IS_SELECT_LANGUAGE,value).apply()


    override fun isSelectedLang()
            =mPrefs.getBoolean(PREF_KEY_IS_SELECT_LANGUAGE,false)
}