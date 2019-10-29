package com.example.lifetime.ui.main.main_activity

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.navigation.*
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseActivity
import com.example.lifetime.ui.addperson.AddPersonDialog
import com.example.lifetime.ui.main.life_spiral_fragment.view.notifayHideLifeSpiralView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.bottom_sheet_person.*
import org.jetbrains.anko.contentView
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

        addPerson.setOnClickListener {
            presenter.onButtonClicked()
        }

        navController = findNavController(R.id.navigationHostFragment)
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

        sheetBehavior = BottomSheetBehavior.from(bottomSheet)


        hideBottomSheet.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }

        var withHand = false
        ivPersons.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                val param = bottomSheet.layoutParams
                param.height = (0.95 * contentView?.height!!).toInt() - mToolbar.height - mToolbar.marginBottom - mToolbar.paddingBottom
                bottomSheet.layoutParams = param
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                notifayHideLifeSpiralView.onNext(false)

            } else {
                withHand = true
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        presenter.getPersons()
        presenter.getLastPersonFromDb()

        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) = Unit

            override fun onStateChanged(p0: View, p1: Int) {
                if (p1 == BottomSheetBehavior.STATE_COLLAPSED) {
                    if (!withHand) notifayHideLifeSpiralView.onNext(true)

                    withHand=false
                }
            }

        })

    }

    override fun getLastPerson(person: Person) {
        this.person = person
        toolbarTitle.text = person?.name
        navController.navigate(R.id.navigationHome)
        navController.popBackStack()

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
        presenter.setLastPersonOnDb(person)
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        navController.navigate(R.id.navigationHome)
        navController.popBackStack()
    }


    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    interface MyDialogDismiss {
        fun getPerson(person: Person, isForUpdate: Boolean)
    }

}

