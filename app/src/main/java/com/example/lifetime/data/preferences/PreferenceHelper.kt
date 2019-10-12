package info.vazeh.android.data.preferences


import com.example.lifetime.util.LoggedInMode


interface PreferenceHelper {
    fun getCurrentUserLoggedInMode(): Int
    fun setCurrentUserLoggedInMode(mode: LoggedInMode)

    fun getAuthorization() : String
    fun setAuthorization(auth: String)

    fun setIsCopiedDatabase(isCopied :Boolean)
    fun isCopiedDatabase() : Boolean

    fun setTranslationMode(isTranslationMode: Boolean)
    fun isTranslationMode(): Boolean


    fun setIsSelectedLang(value: Boolean)
    fun isSelectedLang(): Boolean
}