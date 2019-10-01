package com.example.lifetime.ui.main.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import com.example.lifetime.R
import com.example.lifetime.data.database.AppDatabase
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.addperson.view.AddPersonDialog
import com.example.lifetime.ui.main.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.presenter.MainMVPPresenter
import kotlinx.android.synthetic.main.dialog_add_person.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainMVPView {

    private lateinit var button: AppCompatButton

    @Inject
    lateinit var presenter: MainMVPPresenter<MainMVPView, MainMVPInteractor>

    @Inject
    lateinit var dataBase: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onAttach(this)

        button = findViewById(R.id.button)
        button.setOnClickListener{
            presenter.onButtonClicked()
        }
        presenter.getPersons()
    }

    override fun logPersons(persons: List<Person>) {
        for (person in persons) {
            Log.d("Person",person.name)
        }
    }


    override fun openUserDialog() {
        AddPersonDialog().show(supportFragmentManager,null)
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }


}
