package com.example.lifetime.ui.main.main_activity

import android.graphics.Bitmap
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
import com.example.lifetime.ui.main.life_spiral_fragment.view.LifeSpiralView
import com.example.lifetime.util.AppLogger
import com.example.lifetime.util.LocaleController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.bottom_sheet_person.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainInteractor.MainMVPView,LifeSpiralView.LifeSpiralCallBack {


    lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var presenter: MainInteractor.MainMVPPresenter<MainInteractor.MainMVPView>

    @Inject
    lateinit var adapter: PersonAdapter

    private var persons: MutableList<Person>? = null
    private var person: Person? = null
    private lateinit var navController: NavController
    private var bitmap: Bitmap? = null


    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    private var lifeSpiralFragment: LifeSpiralFragment? = null

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
        setRealTimeText(person)
        adapter.setCheckLastPerson(person)
        if (lifeSpiralFragment?.lifeSpiralView == null || this.person == person) {
            return
        }
        if (this.person != null && this.person?.lifeExpectancyYears == person.lifeExpectancyYears && pointList!=null) {
            this.person = person
            lifeSpiralFragment!!.lifeSpiralView!!.clear()
            lifeSpiralFragment!!.lifeSpiralView!!.setParameters(pointList!!, person)
            return
        }

        this.person = person
        lifeSpiralFragment!!.lifeSpiralView?.clear()
        presenter.calculateDrawPointList(
            lifeSpiralFragment!!.lifeSpiralView!!.w!!,
            lifeSpiralFragment!!.lifeSpiralView!!.h!!,
            person
        )
    }

    override fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
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
                (currentDay- birthDay).toString() + " :"
            currentMonth <= 6 -> {
                lifeSpiralFragment?.day?.text =
                    (currentDay+ 31 - birthDay).toString()+ " :"
                --currentMonth
            }
            else -> {
                lifeSpiralFragment?.day?.text =
                    (currentDay+ 30 - birthDay).toString() + " :"
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

        doAsync {
            while (true) {
                uiThread {
                    val date = Date()
                    val hour = date.hours
                    val minute = date.minutes
                    lifeSpiralFragment?.hour?.text = hour.toString() + " :"
                    lifeSpiralFragment?.minute?.text = minute.toString()
                }
                Thread.sleep(1000)
            }

        }



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navigationHostFragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onFragmentAttached() {
    }

    fun onLifeSpiralFragmentResumed(lifeSpiralFragment: LifeSpiralFragment) {
        this.lifeSpiralFragment = lifeSpiralFragment
        this.lifeSpiralFragment!!.lifeSpiralView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                this@MainActivity.lifeSpiralFragment!!.lifeSpiralView!!.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                this@MainActivity.lifeSpiralFragment!!.lifeSpiralView!!.addListener(this@MainActivity)
                if (person != null) setRealTimeText(person!!)
                if (bitmap != null) {
                    this@MainActivity.lifeSpiralFragment!!.lifeSpiralView!!.setParameters(this@MainActivity.pointList!!,this@MainActivity.person!!,this@MainActivity.bitmap!!)
                    return
                }
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
        presenter.getLastPersonFromDb()
        adapter.notifyDataSetChanged()
    }

    override fun updateViewAfterDeleteCurrentPerson(mainPerson: Person) {
        lifeSpiralFragment?.lifeSpiralView?.clear()
        toolbarTitle.text = mainPerson.name
        setRealTimeText(mainPerson)
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
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    override fun setPointList(pointList: List<Point>) {
        this.pointList = pointList

        lifeSpiralFragment?.lifeSpiralView?.setParameters(
            pointList,
            this.person!!
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

