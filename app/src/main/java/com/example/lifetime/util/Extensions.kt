package com.example.lifetime.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

fun Context.changeStatusBarColor(@ColorRes colorRes: Int, activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColorInt(colorRes)
    }
}

fun Context.getColorInt(@ColorRes res: Int) =
    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        resources.getColor(res,theme)
    else
        resources.getColor(res)

fun Context.getCompatDrawable(@DrawableRes id:Int): Drawable? {
    return  AppCompatResources.getDrawable(this,id)
}

fun Context.getCompatDrawableByLocale(name: String): Drawable? {
    val resources = resources
    val nameWithLocale = "${name}_${LocaleController.getInstance().currentLocaleInfo.shortName}"
    val resourceId = resources.getIdentifier(nameWithLocale, "drawable", packageName)
    return AppCompatResources.getDrawable(this, resourceId)
}


