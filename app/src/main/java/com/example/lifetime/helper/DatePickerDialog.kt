package com.example.lifetime.helper

import android.content.Context
import android.graphics.Color
import com.example.lifetime.R
import com.example.lifetime.util.LocaleController
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar

class DatePickerDialog(val context: Context?, listener: Listener,date: Long?) {
    private val picker: PersianDatePickerDialog = PersianDatePickerDialog(context)
        .setPositiveButtonString(LocaleController.getString(R.string.ok))
        .setNegativeButton(LocaleController.getString(R.string.cancel))
        .setTodayButton(LocaleController.getString(R.string.today))
        .setTodayButtonVisible(true)
        .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
        .setInitDate(if(date==null) PersianCalendar() else PersianCalendar(date))
        .setMinYear(1300)
        .setActionTextColor(Color.GRAY)
        .setListener(listener)
    fun show() {
        picker.show()
    }
}