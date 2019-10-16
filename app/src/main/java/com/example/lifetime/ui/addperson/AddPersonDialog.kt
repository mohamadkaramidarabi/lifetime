package com.example.lifetime.ui.addperson

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.example.lifetime.ui.main.main_activity.MainActivity
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.Listener


class AddPersonDialog(private val myDialogDismiss: MainActivity.MyDialogDismiss) : BaseDialogView(),
    AddPersonInteractor.AddPersonMVPDialog {

    private lateinit var birthDate: PersianCalendar
    private var isDateSated = false
    private lateinit var lifeExpectancies: List<LifeExpectancy>

    @Inject
    lateinit var presenter: AddPersonInteractor.AddPersonMVPPresenter<AddPersonInteractor.AddPersonMVPDialog>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        presenter.loadAllCountries()
        submitButton.setOnClickListener {

            if (isDateSated) {
                var person: Person? = null
                if (countrySelectView.isVisible) {
                    lifeExpectancies.filter {
                        it.country == spinner.selectedItem.toString()
                    }.map {
                        person = Person(
                            nameEdt.text.toString(),
                            it.lifeExpectancy,
                            birthDate.time.time,
                            it.country
                        )

                    }
                } else {
                    person = Person(
                        nameEdt.text.toString(),
                        years.text.toString().toFloat(),
                        birthDate.time.time,
                        null
                    )
                }

                presenter.onSubmitButtonClicked(person!!)
                myDialogDismiss.getPerson(person!!)
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
    }

    override fun openDataPickerView() {
        Toast.makeText(this.context,"Clicked",Toast.LENGTH_LONG).show()
        val picker = PersianDatePickerDialog(this.context)
            .setPositiveButtonString("باشه")
            .setNegativeButton("بیخیال")
            .setTodayButton("امروز")
            .setTodayButtonVisible(true)
            .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
            .setMinYear(1300)
            .setActionTextColor(Color.GRAY)
            .setListener(object : Listener {
                override fun onDateSelected(persianCalendar: PersianCalendar) {
                    presenter.onDateSelected(persianCalendar)
                    isDateSated = true
                }

                override fun onDismissed() {

                }
            })

        picker.show()
    }

    override fun onDateSelected(persianCalendar: PersianCalendar) {
        birthDatePicker.text =
            persianCalendar.persianShortDateTime
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
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

}
