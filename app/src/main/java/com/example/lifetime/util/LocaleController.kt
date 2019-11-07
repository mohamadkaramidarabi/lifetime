package com.example.lifetime.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.example.lifetime.BaseApplication
import com.example.lifetime.R
import java.lang.Exception
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class LocaleController private constructor(){
    var languages = ArrayList<LocaleInfo>()
    var mPref: SharedPreferences = BaseApplication.get().applicationContext.getSharedPreferences(PREF_SETTING_NAME, Context.MODE_PRIVATE)
    var currentLocal: Locale? = null
    var currentResources: Resources? = null
    var currentLocaleInfo: LocaleInfo
    var numberFormat: NumberFormat? = null

    companion object {
        val LocaleGravity: Int
            get() = if (isRtl) Gravity.RIGHT else Gravity.LEFT

        const val TAG = "LocaleController"
        const val KEY_PREF_LANG = "language"
        const val ENGLISH_INDEX = 0
        const val PERSIAN_INDEX = 1

        const val FA = "fa"
        const val EN = "en"

        private var mInstance: LocaleController? =null


        @JvmStatic
        val isRtl: Boolean
            get() {
                return getInstance().currentLocaleInfo.isRtl
            }

        @JvmStatic
        fun getInstance(): LocaleController {
            var localeController: LocaleController? = mInstance
            if (localeController == null) {
                synchronized(LocaleController::class.java) { }
                localeController = mInstance
                if (localeController == null) {
                    localeController = LocaleController()
                    mInstance = localeController
                }
            }
            return localeController!!
        }

        fun getString(id: Int): String = getInstance().getString(id)

        fun getString(id: Int, vararg: Any?) = String.format(getInstance().getString(id), vararg)

        fun getNumber(number: Int) = getInstance().numberFormat?.format(number)

        fun getNumber(number: Long) = getInstance().numberFormat?.format(number)

        fun getEnglishNumber(number: Int)= NumberFormat.getInstance(Locale.US).format(number)

        fun getPersianNumber(number: Int) = NumberFormat.getInstance(Locale(FA,"IR")).format(number)

        fun isPersian() = getInstance().currentLocaleInfo.shortName == FA
        fun isEnglish() = getInstance().currentLocaleInfo.shortName == EN


    }

    init {
        var localInfo = LocaleInfo()
        localInfo.name = "English"
        localInfo.nameEnglish = getString(R.string.english)
        localInfo.pluralLangCode = EN
        localInfo.shortName = localInfo.pluralLangCode
        languages.add(localInfo)

        localInfo = LocaleInfo()
        localInfo.name = "فارسی"
        localInfo.nameEnglish = "Persian"
        localInfo.pluralLangCode = FA
        localInfo.shortName = localInfo.pluralLangCode
        localInfo.isRtl = true
        languages.add(localInfo)

        currentLocaleInfo = getCurrentLang()
    }

    fun getCurrentLang() = when (mPref.getString(KEY_PREF_LANG, FA) ?: FA) {
        FA -> languages[PERSIAN_INDEX]
        else -> languages[ENGLISH_INDEX]
    }


    class LocaleInfo {
        var name: String = ""
        var nameEnglish: String = ""
        var shortName: String = ""
        var baseLnagCode: String? = null
        var pluralLangCode: String = ""
        var isRtl: Boolean = false
        var version: Int = 0

        val langCode: String
            get() = shortName.replace("_", "-")
    }

    private fun getString(@StringRes res: Int) = currentResources?.let {
        it.getString(res)} ?: kotlin.run { BaseApplication.get().applicationContext.getString(res) }

    private fun getStringArray(@ArrayRes res: Int) =
        currentResources?.let {
            listOf(*it.getStringArray(res))
        } ?: kotlin.run {
            listOf(*BaseApplication.get().applicationContext.resources.getStringArray(res))
        }

    fun applyLanguage(localeInfo: LocaleInfo?, mResources: Resources?) {
        if (localeInfo == null || mResources == null) {
            return
        }
        try {
            val editor = mPref.edit()
            editor.putString(KEY_PREF_LANG, localeInfo.shortName)
            editor.apply()

            currentResources = mResources
            currentLocal = Locale(localeInfo.shortName)
            currentLocaleInfo = localeInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun applyLanguage(activity: Activity, localeInfo: LocaleInfo?) {
        if(localeInfo== null) return

        try {
            var context = activity as Context
            val editor = mPref.edit()
            editor.putString(KEY_PREF_LANG, localeInfo.shortName)
            editor.apply()

            val locale = Locale(localeInfo.shortName)
            Locale.setDefault(locale)
            val conf = activity.resources.configuration
            conf.locale = Locale(localeInfo.shortName)
            conf.setLayoutDirection(locale)
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            currentResources = Resources(activity.assets, metrics, conf)
            currentLocal = Locale(localeInfo.shortName)
            currentLocaleInfo = localeInfo

            if (Build.VERSION.SDK_INT >= 25) {
                context = context.applicationContext.createConfigurationContext(conf)
                context = context.createConfigurationContext(conf)
            }
            context.resources.updateConfiguration(conf, context.resources.displayMetrics)

            numberFormat = when {
                localeInfo.shortName == FA -> NumberFormat.getInstance(Locale(FA, "IR"))
                else -> NumberFormat.getInstance(Locale(EN, "US"))

            }
            AndroidUtilities.checkDisplaySize(activity)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}