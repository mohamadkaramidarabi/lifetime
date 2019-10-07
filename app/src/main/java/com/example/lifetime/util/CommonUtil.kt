package com.example.lifetime.util

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.widget.ProgressBar
import androidx.core.graphics.drawable.toDrawable
import com.example.lifetime.R
import ir.hamsaa.persiandatepicker.util.PersianCalendar

object CommonUtil {

    fun showLoadingDialog(context: Context?): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.progress_dialog)
            it.isIndeterminate = true
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }

    fun calculateAge(persianCalendar: PersianCalendar): Int =
        ((PersianCalendar().time.time - persianCalendar.time.time) / (24 * 60 * 60 * 1000)).toInt()


}