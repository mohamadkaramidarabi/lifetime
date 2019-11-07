package com.example.lifetime.ui.main.main_activity

import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
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
import ir.hamsaa.persiandatepicker.util.PersianCalendar
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
    private var lifeExceptionYears: Float? = null
    private lateinit var navController: NavController


    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    private var pointList: List<Point>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mToolbar)
        presenter.onAttach(this)
        bottomNavigationViewSetup()
        bottomSheetSetup()
        presenter.getPersons()
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
                    navController.navigate(R.id.navigationHome)
                    true
                }

                R.id.navigationMessage -> {
                    lifeSpiralFragment?.lifeSpiralView?.clear()
                    mToolbar.visibility = View.VISIBLE
                    ivPersons.visibility = View.GONE
                    toolbarTitle.text = LocaleController.getString(R.string.messages)
                    navController.navigate(R.id.navigationMessage)

                    true
                }

                R.id.navigationAbout -> {
                    lifeSpiralFragment?.lifeSpiralView?.clear()
                    ivPersons.visibility = View.GONE
                    mToolbar.visibility = View.GONE
                    navController.navigate(R.id.navigationAbout)
                    true
                }
                else ->
                    false

            }
        }
    }
    private var drawAfterCollapsed = false

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.navigationHome) {
            finish()
        } else {
            navController.navigate(R.id.navigationHome)
        }
    }

    private fun bottomSheetSetup() {
        sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        hideBottomSheet.setOnClickListener {
            drawAfterCollapsed = true
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        }

        ivPersons.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
//                lifeSpiralFragment?.lifeSpiralView?.clear()
                val param = bottomSheet.layoutParams
                param.height =
                    (0.95 * contentView?.height!!).toInt() - mToolbar.height - mToolbar.marginBottom - mToolbar.paddingBottom
                bottomSheet.layoutParams = param
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

            } else {
                drawAfterCollapsed = true
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) = Unit

            override fun onStateChanged(p0: View, p1: Int) {
                if (p1 == BottomSheetBehavior.STATE_COLLAPSED) {
                    presenter.getLastPersonFromDb()


                }
            }

        })

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
        this.person = person
        toolbarTitle.text = person.name
        setRealTimeText(person)
        lifeExceptionYears = person.LifeExpectancyYears
        if (lifeSpiralFragment?.lifeSpiralView == null) {
            return
        }
        presenter.calculateDrawPointList(
            lifeSpiralFragment?.lifeSpiralView?.w!!,
            lifeSpiralFragment?.lifeSpiralView?.h!!,
            person
        )
    }

    private fun setRealTimeText(person: Person) {
        val birthDate = PersianCalendar(person.birthDate)
        val birthYear = birthDate.persianYear
        val birthMonth = birthDate.persianMonth
        val birthDay = birthDate.persianDay
        val currentDate = PersianCalendar()
        var currentYear = currentDate.persianYear
        var currentMonth = currentDate.persianMonth
        val currentDay = currentDate.persianDay
        when {
            currentDay >= birthDay -> lifeSpiralFragment?.day?.text =
                (currentDay- birthDay).toString()
            currentMonth <= 6 -> {
                lifeSpiralFragment?.day?.text =
                    (currentDay+ 31 - birthDay).toString()
                --currentMonth
            }
            else -> {
                lifeSpiralFragment?.day?.text =
                    (currentDay+ 30 - birthDay).toString()
                --currentMonth
            }
        }
        if (currentMonth >= birthMonth) {
            lifeSpiralFragment?.month?.text = "${currentMonth - birthMonth} :"
        } else {
            lifeSpiralFragment?.month?.text = "${currentMonth + 12  - birthMonth} :"
            --currentYear
        }
        lifeSpiralFragment?.year?.text = "${currentYear - birthYear} :"
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navigationHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private var lifeSpiralFragment: LifeSpiralFragment? = null

    override fun onFragmentAttached() {
    }

    fun onLifeSpiralFragmentResumed(lifeSpiralFragment: LifeSpiralFragment) {
        drawAfterCollapsed = false
        this.lifeSpiralFragment = lifeSpiralFragment
        lifeSpiralFragment.lifeSpiralView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                this@MainActivity.lifeSpiralFragment!!.lifeSpiralView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                if (person == null) {
                    presenter.getLastPersonFromDb()
                } else {
                    getLastPerson(person!!)
                }
            }

        })

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
                } else {
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
        }, person)

        dialog.show(supportFragmentManager, null)
    }

    override fun getPersonFromList(person: Person) {
        toolbarTitle.text = person.name
        drawAfterCollapsed = true

        this.person = person
        presenter.setLastPersonOnDb(person)
        if (lifeExceptionYears != person.LifeExpectancyYears) {
            lifeExceptionYears = person.LifeExpectancyYears
        }

        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    override fun setPointList(pointList: List<Point>) {
        this.pointList = pointList

        lifeSpiralFragment?.lifeSpiralView?.setParameters(
            pointList,
            person!!
        )
    }


    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    interface MyDialogDismiss {
        fun getPerson(person: Person, isForUpdate: Boolean)
    }

}

