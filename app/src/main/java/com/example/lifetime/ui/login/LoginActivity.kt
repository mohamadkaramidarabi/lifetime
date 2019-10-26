package com.example.lifetime.ui.login

import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.helper.DatePickerDialog
import com.example.lifetime.ui.base.view.BaseActivity
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.birthDatePicker
import kotlinx.android.synthetic.main.dialog_add_person.*
import org.jetbrains.anko.toast
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginInteractor.LoginMVPView{

    @Inject
    lateinit var presenter: LoginInteractor.LoginMVPPresenter<LoginInteractor.LoginMVPView>

    private var countriesAdapter : ArrayAdapter<String>? = null
    private var persianCalendar: PersianCalendar? = null
    private var countries: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.onAttach(this)
        setUp()
    }

    private fun setUp() {
        presenter.loadAllCountries()
        birthDatePicker.setOnClickListener {
           presenter.getView()?.onDatePickerClicked()
        }
        btnSubmit.setOnClickListener{
            presenter.getView()?.onRegisterButtonClicked()
        }

    }

    override fun onDatePickerClicked() {
        DatePickerDialog(this,
            object : Listener{
                override fun onDateSelected(calendar: PersianCalendar?) {
                    persianCalendar=calendar
                    birthDatePicker.text = calendar?.persianShortDate
                }

                override fun onDismissed() {

                }

            }).show()
    }


    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {
    }

    override fun openMainActivity() {
    }

    override fun onRegisterButtonClicked() {
        if (etName.text.toString().isEmpty()) {
            toast("Please enter your name!")
            etName.requestFocus()
            return
        }
        if (persianCalendar == null) {
            toast("Please enter your birth date")
            birthDatePicker.requestFocus()
            return
        }
        if (etCountry.text.isEmpty()) {
            toast("Please select your country")
            etCountry.requestFocus()
            return
        }
        var isTrueCountry = false
        countries?.filter {
            etCountry.text.toString() == it
        }?.map {
            isTrueCountry = etCountry.text.toString() == it
        }
        if (!isTrueCountry) {
            toast("Please select country from list!")
            return
        }
        val person= Person(etName.text.toString(),50f,10,etCountry.text.toString())
        person.isMainUser = true
        presenter.addMainPersonToDb(person)
        presenter.setLoggedInState()

    }

    override fun getAllCountries(countries: List<String>) {
        this.countries =countries
        countriesAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ArrayList(countries))

        etCountry.threshold = 1
        etCountry.setAdapter(countriesAdapter)

        etCountry.setOnClickListener {
            etCountry.showDropDown()
        }

    }

}
