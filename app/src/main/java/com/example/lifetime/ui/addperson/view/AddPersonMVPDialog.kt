package com.example.lifetime.ui.addperson.view

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.ui.base.view.MVPView
import ir.hamsaa.persiandatepicker.util.PersianCalendar

interface AddPersonMVPDialog : MVPView{
    fun openDataPickerView()
    fun dismissDialog()
    fun onDateSelected(persianCalendar: PersianCalendar)
    fun getLifeExpectancies(lifeExpectancies: List<LifeExpectancy>)
}