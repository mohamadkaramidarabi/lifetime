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
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_person.*
import javax.inject.Inject
import javax.inject.Named

class MainActivity : BaseActivity(), MainMVPView {

    private lateinit var button: AppCompatButton

    @Inject
    lateinit var presenter: MainMVPPresenter<MainMVPView, MainMVPInteractor>

    @Inject
    lateinit var dataBase: AppDatabase

    private lateinit var dialog: AddPersonDialog


    @Inject
    lateinit var ageByDay: Observable<Int>


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.onAttach(this)
        Log.d("ageByDay",ageByDay.toString())
        dialog = AddPersonDialog(object : MyDialogDismiss {
            override fun getAgeByDay(ageByDay: Int) {
                lifeSpiral.reDraw(ageByDay)
            }
        })

        button = findViewById(R.id.button)
        button.setOnClickListener {
            presenter.onButtonClicked()
        }
    }

    override fun onResume() {
        super.onResume()
        ageByDay.subscribe{
            lifeSpiral.reDraw(it)
        }
        presenter.getPersons()

    }

    override fun loadPersons(persons: List<Person>) {
        for (person in persons) {
            Log.d("Person", person.toString())
        }

    }

    override fun openUserDialog() {
        dialog
            .show(supportFragmentManager, null)
    }




    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }


}

interface MyDialogDismiss {
    fun getAgeByDay(ageByDay: Int)
}
