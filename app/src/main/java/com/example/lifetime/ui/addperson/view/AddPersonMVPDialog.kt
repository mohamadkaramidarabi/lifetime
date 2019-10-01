package com.example.lifetime.ui.addperson.view

import com.example.lifetime.ui.base.view.MVPView

interface AddPersonMVPDialog : MVPView{
    fun openDataPickerView()
    fun dismissDialog()
}