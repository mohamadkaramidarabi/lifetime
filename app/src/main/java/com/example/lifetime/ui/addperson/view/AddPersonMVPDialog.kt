package com.example.lifetime.ui.addperson.view

import com.example.lifetime.ui.base.view.MVPView
import ir.hamsaa.persiandatepicker.util.PersianCalendar

interface AddPersonMVPDialog : MVPView{
    fun openDataPickerView()
    fun dismissDialog()
    fun onDateSelected(birthDate: String, ageByDay: Int)
}