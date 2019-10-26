package com.example.lifetime.helper

import android.content.Context
import android.graphics.Color
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog

class DatePickerDialog(val context: Context?, listener: Listener) {
    private val picker: PersianDatePickerDialog = PersianDatePickerDialog(context)
        .setPositiveButtonString("باشه")
        .setNegativeButton("بیخیال")
        .setTodayButton("امروز")
        .setTodayButtonVisible(true)
        .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
        .setMinYear(1300)
        .setActionTextColor(Color.GRAY)
        .setListener(listener)
    fun show() {
        picker.show()
    }
}