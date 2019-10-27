package com.example.lifetime.ui.main.main_activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.bottom_sheet_person.*
import kotlinx.android.synthetic.main.dialog_add_person.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import javax.inject.Inject


class MainActivity : BaseActivity(), MainInteractor.MainMVPView {
    lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var presenter: MainInteractor.MainMVPPresenter<MainInteractor.MainMVPView>

    @Inject
    lateinit var adapter: PersonAdapter

    private var persons: MutableList<Person>? = null
    private var person: Person? = null
    private lateinit var navController: NavController


    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mToolbar)
        presenter.onAttach(this)
        presenter.getPersons()

        addPerson.setOnClickListener {
            presenter.onButtonClicked()
        }

        navController = findNavController(R.id.navigationHostFragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigationHome, R.id.navigationMessage, R.id.navigationAbout
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.navigationAbout) {
                toolbarTitle.text ="درباره ما"
                ivPersons.visibility = View.GONE
                return@addOnDestinationChangedListener
            }
            if (destination.id == R.id.navigationMessage) {
                toolbarTitle.text = "پیام‌ها"
                ivPersons.visibility = View.GONE
                return@addOnDestinationChangedListener
            }
            if (destination.id == R.id.navigationHome) {
                toolbarTitle.text = person?.name
                ivPersons.visibility = View.VISIBLE
            }
        }

//        bottomNavigationView.setOnNavigationItemSelectedListener {menuItem ->
//            if (menuItem.itemId == R.id.navigationHome) {
//                toast("clicked")
//            }
//
//            true
//
//        }

        sheetBehavior = BottomSheetBehavior.from(bottomSheet)


        hideBottomSheet.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }

        ivPersons.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }



    }


    override fun onStart() {
        super.onStart()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navigationHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onFragmentAttached() {
    }

    override fun onFragmentDetached(tag: String) {
    }

    override fun deletePersonFromList(person: Person) {
        persons?.remove(person)
        adapter.notifyDataSetChanged()
    }


    override fun loadPersons(persons: MutableList<Person>) {
        this.persons = persons
        persons.map {
            if(it.isMainUser){
                this.person = it
            }

        }
        recyclerView.adapter = adapter.let {
            it.persons = persons
            it.notifyDataSetChanged()
            it
        }
    }

    override fun openUserDialog(person: Person?) {
        val dialog = AddPersonDialog(object :
            MyDialogDismiss {
                override fun getPerson(person: Person, isForUpdate: Boolean) {
                    if (!isForUpdate) {
                        persons?.add(person)
                    }
                    else {
                        var index = 0
                        for (p in persons!!) {
                            if (p == person) {
                                break
                            }
                            index++
                        }
                        persons!![index] = person
                    }
                adapter.notifyDataSetChanged()
            }
        },person)

        dialog.show(supportFragmentManager, null)
    }

    override fun getPersonFromList(person: Person) {
        this.person = person
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val bundle = Bundle()
        bundle.putSerializable("person",person)
        navController.navigate(R.id.navigationHome,bundle)
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

    interface MyDialogDismiss {
        fun getPerson(person: Person, isForUpdate: Boolean)
    }

}

