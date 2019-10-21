package com.example.lifetime.ui.main.life_spiral_fragment.view


import android.graphics.Color
import android.graphics.Path
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lifetime.R
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.view.BaseFragment
import com.example.lifetime.ui.main.main_activity.publishPerson
import io.reactivex.disposables.CompositeDisposable
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlinx.android.synthetic.main.fragment_life_spiral.*
import org.jetbrains.anko.custom.asyncResult
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.uiThread
import javax.inject.Inject
import kotlin.math.*

class LifeSpiralFragment : BaseFragment() {


    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    val person: Person = Person("fake name", 80f, PersianCalendar(1000000000000).time.time)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_life_spiral, container, false)

    override fun setUp() {
        lifeSpiral.visibility = View.GONE
        view?.setBackgroundColor(Color.TRANSPARENT)
//        showLoading()
//        this.lifeSpiral = view?.findViewById(R.id.lifeSpiral)
//        compositeDisposable.add(
//            lifeSpiral!!.finishDraw.compose(
//                SchedulerProvider().ioToMainObservableScheduler()
//            ).doOnNext {
//                if(it) hideLoading() else showLoading()
//            }
//            .subscribe()
//        )
//        calculateSpiralPointList()
        publishPerson.subscribe {
            updateUI(it)
        }
//        lifeSpiral?.reDraw(Person("fake name", 80f, PersianCalendar().time.time)) ?: Unit

    }

    override fun onResume() {
        super.onResume()
        calculateSpiralPointList()
    }

    private fun calculateSpiralPointList(){
        doAsync {
            Thread.sleep(1000)
            val path = Path()
            val p = Point()
            val w = lifeSpiral.width
            val h = lifeSpiral.height
            p.x = w / 2
            p.y = h / 2
            Log.d("doAsync", w.toString())
            Log.d("doAsync", h.toString())
            val pointList: MutableList<Point> = mutableListOf()
            pointList.add(Point(p.x, p.y))

            val lifeExceptionYears = person.LifeExpectancyYears
            val dotRadius = sqrt(0.3 * if(w>=h) w.toDouble().pow(2.0)  else  h.toDouble().pow(2.0)/ (lifeExceptionYears * 52 * 4)).toInt()

            val lastCirclePoint = Point(w / 2, h / 2)
            val maxAngle = getMaxAngle(lifeExceptionYears.toInt(), dotRadius)

            val range = 1..maxAngle * 10
            var count = 0
            for (angle in range) {
                Log.d("doAsync", angle.toString())
                val a = angle.toDouble() / 10.0
                val r = (a / (maxAngle)) * (w / 2.0 * 0.95)
                val newX = w / 2f + r * cos(PI * a / 180)
                val newY = h / 2f - r * sin(PI * a / 180)
                path.quadTo(p.x.toFloat(), p.y.toFloat(), newX.toFloat(), newY.toFloat())
                p.y = newY.toInt()
                p.x = newX.toInt()
                if (sqrt(
                        (lastCirclePoint.x.toDouble() - p.x).pow(2.0) +
                                (lastCirclePoint.y.toDouble() - p.y).pow(2.0)
                    ) >= (2 * dotRadius * 1.2)
                ) {

                    lastCirclePoint.x = p.x
                    lastCirclePoint.y = p.y

                    count++
                    if (count <= (lifeExceptionYears * 365).toDouble() / 7) {
                        pointList.add(Point(p.x, p.y))
                    }
                }
            }
            uiThread {
                lifeSpiral.setParameters(pointList,dotRadius)
                lifeSpiral.visibility = View.VISIBLE
            }
        }

    }

    private fun getMaxAngle(LifeExpectancyYears: Int, dotRadius: Int): Int {
        var phi = 0
        val circleRadius = if (lifeSpiral.width > lifeSpiral.height) lifeSpiral.height else lifeSpiral.width
        while (getSpiralLength(phi * PI / 180,circleRadius).toInt() <= (LifeExpectancyYears * 52 * dotRadius * 2 * 1.28).toInt()) {
            phi += 1
        }
        return phi
    }

    private fun getSpiralLength(phi: Double, circleRadius: Int): Double =
        ((circleRadius / 4.0 * 0.95 / (phi))) * (ln(sqrt(phi.pow(2.0) + 1) + phi) + phi * sqrt(phi.pow(2.0) + 1))

    private fun updateUI(person: Person) {
        lifeSpiral?.reDraw(person)
    }


}
