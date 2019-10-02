package com.example.lifetime.ui.addperson.view

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.addperson.interactor.AddPersonMVPInteractor
import com.example.lifetime.ui.addperson.presenter.AddPersonMVPPresenter
import com.example.lifetime.ui.base.view.BaseDialogView
import kotlinx.android.synthetic.main.dialog_add_person.*
import javax.inject.Inject
import android.widget.Toast
import com.example.lifetime.ui.main.view.MyDialogDismiss
import io.reactivex.Observable
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.Listener


class AddPersonDialog(private val myDialogDismiss: MyDialogDismiss) : BaseDialogView(), AddPersonMVPDialog {

    private var ageByDay: Int = 0
    @Inject
    lateinit var presenter: AddPersonMVPPresenter<AddPersonMVPDialog,AddPersonMVPInteractor>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_add_person, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        submitButton.setOnClickListener {
            presenter.onSubmitButtonClicked(
                Person(nameEdt.text.toString(), ageByDay)
            )
            myDialogDismiss.getAgeByDay(ageByDay)
            dismissDialog()
        }
        birthDatePicker.setOnClickListener {
            presenter.onDatePickerClicked()
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
                }

                override fun onDismissed() {

                }
            })

        picker.show()
    }

    override fun onDateSelected(birthDate: String, ageByDay: Int) {
        birthDatePicker.text =
            birthDate
        this.ageByDay=ageByDay
    }

    override fun dismissDialog() {
        dismiss()
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

}
