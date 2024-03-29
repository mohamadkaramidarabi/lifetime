package com.example.lifetime.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.helper.DatePickerDialog
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.main.main_activity.MainActivity
import com.example.lifetime.util.LocaleController
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.birthDatePicker
import kotlinx.android.synthetic.main.dialog_add_person.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.toast
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class LoginActivity : BaseActivity(), LoginInteractor.LoginMVPView{

    @Inject
    lateinit var presenter: LoginInteractor.LoginMVPPresenter<LoginInteractor.LoginMVPView>

    private var countriesAdapter : ArrayAdapter<String>? = null
    private var persianBirthDate: PersianCalendar? = null
    private var birthDate : Calendar? = null
    private var lifeExpectancies: List<LifeExpectancy>? = null

    private lateinit var nameEdt: EditText
    private lateinit var birthDatePicker : TextView
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.onAttach(this)
        setUp()
    }

    private fun setUp() {
        nameEdt = findViewById(R.id.etName)
        birthDatePicker = findViewById(R.id.birthDatePicker)
        btnSubmit = findViewById(R.id.btnSubmit)

        nameEdt.requestFocus()
        birthDatePicker.setOnClickListener {
            presenter.onDatePickerClicked()
        }
        btnSubmit.setOnClickListener{
            presenter.onRegisterButtonClicked()
        }

        presenter.loadLifeExpectancies()

    }

    override fun openDatePickerDialog() {
        if (LocaleController.getInstance().currentLocaleInfo.shortName == LocaleController.EN) {
            DatePickerDialog(
                this,
                null,
                android.app.DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    birthDate = Calendar.getInstance().let {
                        it.set(Calendar.YEAR,year)
                        it.set(Calendar.MONTH,month)
                        it.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                        it
                    }
                    birthDatePicker.text = "$year/$month/$dayOfMonth"
                    persianBirthDate = PersianCalendar(birthDate?.timeInMillis!!)
                }
                , persianBirthDate?.timeInMillis
            ).show()
        } else {
            DatePickerDialog(
                this,
                DateSetListener { _, calendar, dayOfMonth, month, year ->
                    persianBirthDate = PersianCalendar(calendar?.timeInMillis!!)
                    birthDate = calendar
                    birthDatePicker.text = "$year/$month/$dayOfMonth"
                },
                null
                , persianBirthDate?.timeInMillis
            ).show()
        }
    }


    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {
    }

    override fun openMainActivity() {
        val imm = this.baseContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.contentView?.windowToken, 0)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun checkFormInfo() : Boolean{
        if (etName.text.toString().isEmpty()) {
            toast("Please enter your name!")
            etName.requestFocus()
            return false
        }
        if (persianBirthDate == null || birthDate == null) {
            toast("Please enter your birth date")
            birthDatePicker.requestFocus()
            return false
        }
        if (etCountry.text.isEmpty()) {
            toast("Please select your country")
            etCountry.requestFocus()
            return false
        }
        var isTrueCountry = false
        lifeExpectancies?.map { it.country }?.filter {
            etCountry.text.toString() == it
        }?.map {
            isTrueCountry = etCountry.text.toString() == it
        }
        if (!isTrueCountry) {
            toast("Please select country from list!")
            return false
        }
        if (etEmail.text.toString().isNotEmpty()) {
            val email = etEmail.text.toString()
            if (!email.contains('@') ||
                !email.contains('.') ||
                email.lastIndexOf('.') >= email.length + 2 ||
                email.lastIndexOf('@') != email.indexOf('@') ||
                email.lastIndexOf('.') <= email.indexOf('@') + 1
            ) {
                toast("Wrong Email Format!!")
                etEmail.requestFocus()
                return false

            }
        }
        return true
    }

    override fun createPersonFromForm(): Person {
        val person =
            Person(
                etName.text.toString(),
                lifeExpectancies!!.filter { it.country == etCountry.text.toString() }.map { it.lifeExpectancy }[0],
                persianBirthDate?.time?.time!!,
                etCountry.text.toString()
            )
        person.isMainUser = true
        if (etEmail.text.toString().isNotEmpty()) {
            person.email = etEmail.text.toString()
        }
        return person
    }

    override fun getLifeExpectancies(lifeExpectancies: List<LifeExpectancy>) {
        this.lifeExpectancies =lifeExpectancies
        countriesAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ArrayList(lifeExpectancies.map { it.country }))

        etCountry.threshold = 1
        etCountry.setAdapter(countriesAdapter)

        etCountry.setOnClickListener {
            etCountry.showDropDown()
        }

    }

}
