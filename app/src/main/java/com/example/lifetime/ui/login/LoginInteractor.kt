package com.example.lifetime.ui.login

import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.MVPPresenter
import com.example.lifetime.ui.base.view.MVPView

object LoginInteractor {

    interface LoginMVPView: MVPView {
        fun openMainActivity()
        fun getLifeExpectancies(lifeExpectancies: List<LifeExpectancy>)
        fun openDatePickerDialog()
        fun checkFormInfo(): Boolean
        fun createPersonFromForm(): Person
    }

    interface LoginMVPPresenter<V: LoginMVPView>: MVPPresenter<V>  {
        fun setLoggedInState()
        fun loadLifeExpectancies(): Boolean
        fun addMainPersonToDb(person: Person)
        fun onDatePickerClicked()
        fun onRegisterButtonClicked()
    }

}