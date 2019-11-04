package com.example.lifetime.util

import android.content.Context

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
}