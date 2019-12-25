package com.example.lifetime.ui.main.main_activity

import android.graphics.Bitmap
import android.graphics.Point
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
import com.example.lifetime.ui.main.life_spiral_fragment.view.LifeSpiralFragment
import com.example.lifetime.util.LocaleController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.bottom_sheet_person.*
import org.jetbrains.anko.contentView
import javax.inject.Inject


class MainActivity : BaseActivity(), MainInteractor.MainMVPView {


    lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var presenter: MainInteractor.MainMVPPresenter<MainInteractor.MainMVPView>


    lateinit var adapter: PersonAdapter

    private lateinit var navController: NavController

    private var persons: MutableList<Person>? = null
    var person: Person? = null
    var bitmap: Bitmap? = null
        private set
    var pointList: List<Point>? = null
        private set

    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mToolbar)
        presenter.onAttach(this)
        bottomNavigationViewSetup()
        bottomSheetSetup()
        presenter.getPersons()
        adapter = PersonAdapter {person, action ->
            when (action) {
                PersonAction.VIEW -> presenter.onPersonClicked(person)
                PersonAction.EDIT -> presenter.getView()?.openUserDialog(person)
                PersonAction.DELETE -> presenter.deletePerson(person)
            }
        }
    }

    private var lastFragmentId: Int = R.id.navigationHome
    private fun bottomNavigationViewSetup() {
        navController = findNavController(R.id.navigationHostFragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigationHome, R.id.navigationMessage, R.id.navigationAbout
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            if(lastFragmentId == it.itemId) return@setOnNavigationItemSelectedListener true
            lastFragmentId = it.itemId

            when (it.itemId) {
                R.id.navigationHome -> {
                    toolbarTitle.text = person?.name
                    mToolbar.visibility = View.VISIBLE
                    ivPersons.visibility = View.VISIBLE
                    navController.popBackStack(R.id.navigationHome, true)
                    navController.navigate(R.id.navigationHome)
                    true
                }

                R.id.navigationMessage -> {
                    mToolbar.visibility = View.VISIBLE
                    ivPersons.visibility = View.GONE
                    toolbarTitle.text = LocaleController.getString(R.string.messages)
                    navController.popBackStack(R.id.navigationHome, false)
                    navController.navigate(R.id.navigationMessage)
                    true
                }

                R.id.navigationAbout -> {
                    ivPersons.visibility = View.GONE
                    mToolbar.visibility = View.GONE
                    navController.popBackStack(R.id.navigationHome, false)
                    navController.navigate(R.id.navigationAbout)
                    true
                }
                else ->
                    false

            }
        }
    }


    private fun bottomSheetSetup() {
        sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        hideBottomSheet.setOnClickListener {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }

        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onSlide(p0: View, p1: Float) {

            }

            override fun onStateChanged(p0: View, p1: Int) {
                if (p1 == BottomSheetBehavior.STATE_COLLAPSED) {
                    presenter.getLastPersonFromDb()
                }
            }

        })

        ivPersons.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                val param = bottomSheet.layoutParams
                param.height =
                    (0.95 * contentView?.height!!).toInt() - mToolbar.height - mToolbar.marginBottom - mToolbar.paddingBottom
                bottomSheet.layoutParams = param
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            } else {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        addPerson.setOnClickListener {
            presenter.onButtonClicked()
        }
    }


    override fun onStart() {
        super.onStart()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun getLastPerson(person: Person) {
        toolbarTitle.text = person.name
        if(this.person==null) this.person =person
        else{
            if (!(this.person!!.lifeExpectancyYears == person.lifeExpectancyYears && this.person!!.birthDate == person.birthDate)) {
                bitmap = null
            }
            if (this.person!!.lifeExpectancyYears != person.lifeExpectancyYears) {
                pointList = null
            }
            this.person = person
        }
        adapter.setCheckLastPerson(person)
        (supportFragmentManager.fragments[0].childFragmentManager.fragments[0] as LifeSpiralFragment)
            .setPerson(person)
    }

    fun onLifeSpiralFragmentResumed() {
        mToolbar.visibility = View.VISIBLE
        ivPersons.visibility = View.VISIBLE
        lastFragmentId = R.id.navigationHome
        presenter.getLastPersonFromDb()

    }

    override fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    override fun setPointList(pointList: List<Point>) {
        this.pointList = pointList
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
        presenter.getLastPersonFromDb()
        adapter.notifyDataSetChanged()
    }

    override fun updateViewAfterDeleteCurrentPerson(mainPerson: Person) {
        toolbarTitle.text = mainPerson.name
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
                } else {
                    var index = 0
                    for (p in persons!!) {
                        if (p.id == person.id) {
                            break
                        }
                        index++
                    }
                    persons!![index] = person
                }
                adapter.notifyDataSetChanged()
            }
        }, person)

        dialog.show(supportFragmentManager, null)
    }

    override fun getPersonFromList(person: Person) {
        toolbarTitle.text = person.name
        presenter.setLastPersonOnDb(person)
//        getLastPerson(person)
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    interface MyDialogDismiss {
        fun getPerson(person: Person, isForUpdate: Boolean)
    }



}

