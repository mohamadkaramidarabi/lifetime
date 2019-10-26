package com.example.lifetime.ui.addperson

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseDialogView
import kotlinx.android.synthetic.main.dialog_add_person.*
import javax.inject.Inject
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.helper.DatePickerDialog
import com.example.lifetime.ui.main.main_activity.MainActivity
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import ir.hamsaa.persiandatepicker.Listener


class AddPersonDialog(
    private val myDialogDismiss: MainActivity.MyDialogDismiss,
    private var person: Person? = null
) : BaseDialogView(),
    AddPersonInteractor.AddPersonMVPDialog {

    private lateinit var birthDate: PersianCalendar
    private var isDateSated = false
    private lateinit var lifeExpectancies: List<LifeExpectancy>

    @Inject
    lateinit var presenter: AddPersonInteractor.AddPersonMVPPresenter<AddPersonInteractor.AddPersonMVPDialog>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_person, container, false)
    }

    private val isForUpdate: Boolean = person != null

    override fun isForUpdate(): Boolean = isForUpdate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        presenter.loadAllCountries()
        submitButton.setOnClickListener {

            if (nameEdt.text.trim().isEmpty()) {
                Toast.makeText(this.context,"Please enter person name",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (isDateSated) {
                if (countrySelectView.isVisible) {
                    lifeExpectancies.filter {
                        it.country == spinner.selectedItem.toString()
                    }.map {
                        if (isForUpdate()) {
                            person.let { person ->
                                person?.name = nameEdt.text.toString()
                                person?.LifeExpectancyYears = it.lifeExpectancy
                                person?.birthDate = birthDate.time.time
                                person?.country = it.country
                            }
                        } else {
                            person = Person(
                                nameEdt.text.toString(),
                                it.lifeExpectancy,
                                birthDate.time.time,
                                it.country
                            )
                        }

                    }
                } else {
                    if (isForUpdate()) {
                        person.let { person ->
                            person?.name = nameEdt.text.toString()
                            person?.LifeExpectancyYears = years.text.toString().toFloat()
                            person?.birthDate = birthDate.time.time
                            person?.country = null
                        }
                    } else {
                        person = Person(
                            nameEdt.text.toString(),
                            years.text.toString().toFloat(),
                            birthDate.time.time,
                            null
                        )
                    }
                }

                presenter.onSubmitButtonClicked(person!!)
                myDialogDismiss.getPerson(person!!,isForUpdate())
                dismissDialog()
            } else {
                Toast.makeText(this.context,"Please set birth Date",Toast.LENGTH_LONG).show()
            }
        }
        birthDatePicker.setOnClickListener {
            presenter.onDatePickerClicked()
        }

        lifeExOptions.setOnCheckedChangeListener { _, i ->
            if (i == countryOption.id) {
                countrySelectView.visibility = View.VISIBLE
                enterYearView.visibility = View.GONE
            }else{
                countrySelectView.visibility = View.GONE
                enterYearView.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes = params as android.view.WindowManager.LayoutParams
        }
        if (person == null) {
            nameEdt.setText("")
            isDateSated = false
        }else{
            nameEdt.setText(person?.name)
            isDateSated = true
            birthDate = PersianCalendar(person?.birthDate!!)
            birthDatePicker.text = birthDate.persianShortDate
            submitButton.text = "اصلاح"
            if (person?.country == null) {
                countrySelectView.visibility = View.GONE
                enterYearView.visibility = View.VISIBLE
                lifeExOptions.check(R.id.manualOption)
                years.setText(person?.LifeExpectancyYears.toString())
            }
        }
    }

    override fun openDataPickerView() {
        DatePickerDialog(this.context, object : Listener{
            override fun onDateSelected(persianCalendar: PersianCalendar) {
                presenter.onDateSelected(persianCalendar)
                isDateSated = true
            }

            override fun onDismissed() {

            }

        }).show()
    }

    override fun onDateSelected(persianCalendar: PersianCalendar) {
        birthDatePicker.text =
            persianCalendar.persianShortDate
        this.birthDate = persianCalendar
    }

    override fun dismissDialog() {
        dismiss()
    }

    override fun getLifeExpectancies(lifeExpectancies: List<LifeExpectancy>) {
        this.lifeExpectancies = lifeExpectancies
        lifeExpectancies.map {
            it.country
        }.let {
            val adapter = ArrayAdapter<String>(
                this.context!!,
                android.R.layout.simple_spinner_dropdown_item,
                it
            )
            spinner.adapter = adapter
        }
        var country: String? = null
        country = if (person?.country != null) {
            person?.country
        } else {
            "Iran"
        }
        lifeExpectancies.mapIndexed { index, lifeExpectancy ->
            if(lifeExpectancy.country == country)
                spinner.setSelection(index)
            index
        }
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

}
