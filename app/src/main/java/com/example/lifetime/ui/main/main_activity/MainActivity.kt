package com.example.lifetime.ui.main.main_activity

import android.os.Bundle
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.addperson.AddPersonDialog
import com.example.lifetime.ui.main.life_spiral_fragment.view.LifeSpiralFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_life_spiral.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainInteractor.MainMVPView {
    lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var presenter: MainInteractor.MainMVPPresenter<MainInteractor.MainMVPView>

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


        dialog = AddPersonDialog(object :
            MyDialogDismiss {
            override fun getPerson(person: Person) {
                persons?.add(person)
                adapter.notifyDataSetChanged()
                lifeSpiral.reDraw(person)
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

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {
    }


    override fun loadPersons(persons: MutableList<Person>) {
        this.persons = persons
        personListRecyclerView.adapter = adapter.let {
            it.persons = persons
            it.notifyDataSetChanged()
            it
        }
        for (fragment in nav_host_fragment.childFragmentManager.fragments) {
            if (fragment is LifeSpiralFragment) {
                fragment.updateUI(persons[persons.lastIndex])
                break
            }
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
