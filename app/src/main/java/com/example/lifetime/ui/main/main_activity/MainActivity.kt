package com.example.lifetime.ui.main.main_activity

import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.addperson.AddPersonDialog
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_life_spiral.*
import kotlinx.android.synthetic.main.person_bottom_sheet.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import javax.inject.Inject

class MainActivity : BaseActivity(), MainInteractor.MainMVPView {
    lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var presenter: MainInteractor.MainMVPPresenter<MainInteractor.MainMVPView>

    @Inject
    lateinit var adapter: PersonAdapter

    private lateinit var dialog: AddPersonDialog

    private var persons: MutableList<Person>? = null

    private var touchedByHand = false


    //    private var bottomSheetBehavior = BottomSheetBehavior.from()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

//        addPerson.setOnClickListener {
//            presenter.onButtonClicked()
//        }

        val navController = findNavController(R.id.navigationHostFragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigationHome, R.id.navigationMessage, R.id.navigationAbout
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)

        val sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        navigationView.findViewById<BottomNavigationItemView>(R.id.navigationHome)
            .setOnClickListener {
                toast("clicked")
                if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }
                navController.navigate(R.id.navigationHome)
            }


    }


    override fun onStart() {
        super.onStart()
//        personListRecyclerView.adapter = adapter
//        personListRecyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navigationHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {
    }


    override fun loadPersons(persons: MutableList<Person>) {
        this.persons = persons
//        personListRecyclerView.adapter = adapter.let {
//            it.persons = persons
//            it.notifyDataSetChanged()
//            it
//        }
//        for (fragment in nav_host_fragment.childFragmentManager.fragments) {
//            if (fragment is LifeSpiralFragment) {
//                fragment.updateUI(persons[persons.lastIndex])
//                break
//            }
//        }
    }

    override fun openUserDialog() {
        dialog.show(supportFragmentManager, null)
    }



    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        toast("clicked")
        if (item?.itemId == R.id.navigationHome) {
            longToast("home")
        }
        return super.onContextItemSelected(item)

    }
}
interface MyDialogDismiss {
    fun getPerson(person: Person)
}
