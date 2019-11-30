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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.alirezaafkar.sundatepicker.interfaces.DateSetListener
import com.example.lifetime.data.database.repository.life_expectancies.LifeExpectancy
import com.example.lifetime.helper.DatePickerDialog
import com.example.lifetime.ui.main.main_activity.MainActivity
import com.example.lifetime.util.LocaleController
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlinx.android.synthetic.main.dialog_add_person.birthDatePicker
import java.util.*


class AddPersonDialog(
    private val myDialogDismiss: MainActivity.MyDialogDismiss,
    private var person: Person? = null
) : BaseDialogView(),
    AddPersonInteractor.AddPersonMVPDialog {

    private var persianBirthDate: PersianCalendar? = null
    private var birthDate: Calendar? = null
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
                                person?.lifeExpectancyYears = it.lifeExpectancy
                                person?.birthDate = persianBirthDate?.timeInMillis!!
                                person?.country = it.country
                            }
                        } else {
                            person = Person(
                                nameEdt.text.toString(),
                                it.lifeExpectancy,
                                persianBirthDate?.timeInMillis!!,
                                it.country
                            )
                        }

                    }
                } else {
                    if (isForUpdate()) {
                        person.let { person ->
                            person?.name = nameEdt.text.toString()
                            person?.lifeExpectancyYears = years.text.toString().toFloat()
                            person?.birthDate = persianBirthDate?.timeInMillis!!
                            person?.country = null
                        }
                    } else {
                        person = Person(
                            nameEdt.text.toString(),
                            years.text.toString().toFloat(),
                            persianBirthDate?.timeInMillis!!,
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
            persianBirthDate = PersianCalendar(person!!.birthDate)
            birthDate = Calendar.getInstance().let {
                it.timeInMillis = persianBirthDate!!.timeInMillis
                it
            }
            birthDatePicker.text = if (LocaleController.isEnglish()) {
                "${birthDate?.get(Calendar.YEAR)}/${birthDate?.get(Calendar.MONTH)}/${birthDate?.get(Calendar.DAY_OF_MONTH)}"
            } else {
                "${persianBirthDate?.persianYear}/${persianBirthDate?.persianMonth}/${persianBirthDate?.persianDay}"
            }
            submitButton.text = LocaleController.getString(R.string.edit)
            tvTitle.text = LocaleController.getString(R.string.information_edit)
            if (person?.country == null) {
                countrySelectView.visibility = View.GONE
                enterYearView.visibility = View.VISIBLE
                lifeExOptions.check(R.id.manualOption)
                years.setText(person?.lifeExpectancyYears.toString())
            }
        }

        btnClose.setOnClickListener{
            dismissDialog()
        }
    }

    override fun openDataPickerView() {
        if (LocaleController.getInstance().currentLocaleInfo.shortName == LocaleController.EN) {
            DatePickerDialog(
                this.activity as AppCompatActivity,
                null,
                android.app.DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    birthDate = Calendar.getInstance().let {
                        isDateSated = true
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
                this.activity as AppCompatActivity,
                DateSetListener { _, calendar, dayOfMonth, month, year ->
                    isDateSated = true
                    persianBirthDate = PersianCalendar(calendar?.timeInMillis!!)
                    birthDate = calendar
                    birthDatePicker.text = "$year/$month/$dayOfMonth"
                },
                null
                , persianBirthDate?.timeInMillis
            ).show()
        }
    }

    override fun onDateSelected(persianCalendar: PersianCalendar) {
        birthDatePicker.text =
            persianCalendar.persianShortDate
        this.persianBirthDate = persianCalendar
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
        val country: String? = if (person?.country != null) {
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
