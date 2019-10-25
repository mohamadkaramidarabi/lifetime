package info.vazeh.android.data.preferences


import com.example.lifetime.util.LoggedInMode


interface PreferenceHelper {
    fun getCurrentUserLoggedInMode(): Int
    fun setCurrentUserLoggedInMode(mode: LoggedInMode)
}