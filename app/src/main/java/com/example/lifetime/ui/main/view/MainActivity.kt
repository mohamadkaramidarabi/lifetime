package com.example.lifetime.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifetime.R
import com.example.lifetime.data.database.AppDatabase
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.addperson.view.AddPersonDialog
import com.example.lifetime.ui.lifespiralview.LifeSpiral
import com.example.lifetime.ui.lifespiralview.LifeSpiralFragment
import com.example.lifetime.ui.main.interactor.MainMVPInteractor
import com.example.lifetime.ui.main.presenter.MainMVPPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_life_spiral.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainMVPView {

    lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var presenter: MainMVPPresenter<MainMVPView, MainMVPInteractor>

    @Inject
    lateinit var adapter: PersonAdapter

    private lateinit var dialog: AddPersonDialog

    private var persons: MutableList<Person>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        presenter.onAttach(this)
        presenter.getPersons()


        dialog = AddPersonDialog(object : MyDialogDismiss {
            override fun getPerson(person: Person) {
                lifeSpiral.reDraw(person)
                persons?.add(person)
                adapter.notifyDataSetChanged()
            }
        })

        addPerson.setOnClickListener {
            presenter.onButtonClicked()
        }

        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ),drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        personListRecyclerView.adapter = adapter
        personListRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun loadPersons(persons: MutableList<Person>) {
        Log.d("TAG",persons.toString())
        this.persons = persons
        lifeSpiral.visibility=View.VISIBLE
//        lifeSpiral.reDraw(persons[persons.lastIndex])
        personListRecyclerView.adapter = adapter.let {
            it.persons = persons
            it.notifyDataSetChanged()
            it
        }
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
    fun getPerson(person: Person)
}
