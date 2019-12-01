package com.example.lifetime.ui.main.life_spiral_fragment.view


import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseFragment
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import java.util.concurrent.Future
import javax.inject.Inject


class LifeSpiralFragment : BaseFragment(), LifeSpiralInteractor.LifeSpiralMVPView{



    @Inject
    lateinit var presenter: LifeSpiralInteractor.LifeSpiralMVPPresenter<LifeSpiralInteractor.LifeSpiralMVPView>

    var person: Person? = null
    lateinit var lifeSpiralView: LifeSpiralView
        private set

    lateinit var year: TextView
        private set
    lateinit var month: TextView
        private set
    lateinit var day: TextView
        private set
    lateinit var minute: TextView
        private set
    lateinit var hour: TextView
        private set

    private var bitmap: Bitmap? = null
    private var pointList: List<Point>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_life_spiral, container, false)


    override fun setUp() {
        presenter.onAttach(this)
        view!!.setBackgroundColor(Color.TRANSPARENT)
        lifeSpiralView = view!!.findViewById(R.id.lifeSpiral)
        year = view!!.findViewById(R.id.yearTextView)
        month = view!!.findViewById(R.id.monthTextView)
        day = view!!.findViewById(R.id.dayTextView)
        hour = view!!.findViewById(R.id.hourTextView)
        minute = view!!.findViewById(R.id.minuteTextView)
        presenter.getLastPerson()
    }

    override fun getPerson(person: Person) {
        this.person = person
        this.lifeSpiralView.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                this@LifeSpiralFragment.lifeSpiralView.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                presenter.calculateDrawPointList(lifeSpiralView.measuredWidth!!, lifeSpiralView.measuredHeight!!, person)
            }

        })
        if (this@LifeSpiralFragment.person != null) setRealTimeText(this@LifeSpiralFragment.person!!)
        if (bitmap != null) {
            this@LifeSpiralFragment.lifeSpiralView.setParameters(this@LifeSpiralFragment.pointList!!,this@LifeSpiralFragment.person!!,this@LifeSpiralFragment.bitmap!!)
            return
        }
//        if (this@LifeSpiralFragment.person == null) {
//            presenter.getLastPerson()
//        } else {
//            getPerson(this@LifeSpiralFragment.person!!)
//        }

    }


    override fun setPointList(pointList: List<Point>) {
        this.pointList = pointList
        lifeSpiralView.setParameters(this.pointList!!, person!!)
    }



    override fun onDetach() {
        presenter.onDetach()
        future?.cancel(true)
        super.onDetach()
    }

    private var future: Future<Unit>? = null
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
            currentDay >= birthDay -> day.text =
                (currentDay - birthDay).toString() + " :"
            currentMonth <= 6 -> {
                day.text =
                    (currentDay + 31 - birthDay).toString() + " :"
                --currentMonth
            }
            else -> {
                day.text =
                    (currentDay + 30 - birthDay).toString() + " :"
                --currentMonth
            }
        }
        if (currentMonth >= birthMonth) {
            month.text = "${currentMonth - birthMonth} :"
        } else {
            month.text = "${currentMonth + 12 - birthMonth} :"
            --currentYear
        }
        year.text = "${currentYear - birthYear} :"

        future = doAsync {
            while (true) {
                uiThread {
                    val date = Date()
                    val hour = date.hours
                    val minute = date.minutes
                    this@LifeSpiralFragment.hour.text = hour.toString() + " :"
                    this@LifeSpiralFragment.minute.text = minute.toString()
                }
                Thread.sleep(1000)
            }
        }
    }
}
