package com.example.lifetime.helper

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import com.alirezaafkar.sundatepicker.DatePicker as PersianDatePicker
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener as PersianDateSetListener
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import java.util.*

class DatePickerDialog(
    private val activity: AppCompatActivity,
    persianDateSetListener: PersianDateSetListener? = null,
    onDateSetListener: DatePickerDialog.OnDateSetListener? = null,
    date: Long?) {

    private val calender: Calendar
    private val persianCalender: PersianCalendar

    init {
        persianCalender = if(date!=null ) PersianCalendar(date) else PersianCalendar()
        calender = Calendar.getInstance().let {
            it.time = persianCalender.time
            it
        }
    }


    private val datePicker = if (persianDateSetListener != null) {
        PersianDatePicker.Builder()
            .date(if (date == null) PersianCalendar() else PersianCalendar(date))
            .maxDate(Calendar.getInstance())
            .build(persianDateSetListener)
    } else {
        DatePickerDialog(
            activity,
            onDateSetListener,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        ).let {
            it.datePicker.maxDate = Calendar.getInstance().timeInMillis
            it
        }
    }



    fun show() {
        if(datePicker is PersianDatePicker) datePicker.show(activity.supportFragmentManager, "")
        else if(datePicker is DatePickerDialog) datePicker.show()
    }
}