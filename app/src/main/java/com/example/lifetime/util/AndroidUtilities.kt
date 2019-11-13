package com.example.lifetime.util

import android.app.Activity
import android.content.Context
import kotlin.math.ceil



object AndroidUtilities {


    @JvmStatic
    var density = 1f
    var mWidth: Int = 0
    var mHeight: Int = 0

    fun checkDisplaySize(context: Context) {
        try {
            density = context.resources.displayMetrics.density
            mWidth = context.resources.displayMetrics.widthPixels
            mHeight = context.resources.displayMetrics.heightPixels

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @JvmStatic
    fun dp(value: Float): Int =
        if(value == 0f) 0
        else ceil((density*value).toDouble()).toInt()

    @JvmStatic
    fun getImage(activity: Activity,imageName: String): Int {
        return activity.resources.getIdentifier(imageName, "drawable", activity.packageName)
    }
}