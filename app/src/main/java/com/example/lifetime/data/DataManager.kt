package com.example.lifetime.data

import com.example.lifetime.data.database.DbHelper
import info.vazeh.android.data.preferences.PreferenceHelper

interface DataManager : DbHelper, PreferenceHelper {

}