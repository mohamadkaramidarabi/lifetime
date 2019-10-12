package com.example.lifetime.data

import com.example.lifetime.data.database.DbHelper
import info.vazeh.android.data.preferences.PreferenceHelper
import javax.inject.Singleton

@Singleton
interface DataManager : DbHelper, PreferenceHelper {

}