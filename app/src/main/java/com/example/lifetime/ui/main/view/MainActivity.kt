package com.example.lifetime.ui.main.view

import android.os.Bundle
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lifetime.R
import com.example.lifetime.data.database.AppDatabase
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.addperson.view.AddPersonDialog
import com.example.lifetime.ui.main.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.presenter.MainMVPPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_life_spiral.*
import java.lang.Exception
import javax.inject.Inject

class MainActivity : BaseActivity(), MainMVPView {

    lateinit var appBarConfiguration: AppBarConfiguration


    @Inject
    lateinit var presenter: MainMVPPresenter<MainMVPView, MainMVPInteractor>

    @Inject
    lateinit var dataBase: AppDatabase

    private lateinit var dialog: AddPersonDialog


    private var persons: List<Person>? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        lifeSpiral.visibility= View.GONE
        presenter.onAttach(this)
        presenter.getPersons()
        setSupportActionBar(toolbar)

        dialog = AddPersonDialog(object : MyDialogDismiss {
            override fun getAgeByDay(ageByDay: Int) {
                lifeSpiral.reDraw(ageByDay)
            }
        })

//        button.setOnClickListener {
//            presenter.onButtonClicked()
//        }

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navView
            ),drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun loadPersons(persons: List<Person>) {
        this.persons = persons
        try {
//            lifeSpiral.ageByDay = persons[persons.lastIndex].age
        } catch (e: Exception) {

        }

//        lifeSpiral.visibility = View.VISIBLE
    }

    override fun openUserDialog() {
        dialog.show(supportFragmentManager, null)
    }





    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }


}

interface MyDialogDismiss {
    fun getAgeByDay(ageByDay: Int)
}
