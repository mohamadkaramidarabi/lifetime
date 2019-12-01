package com.example.lifetime.ui.main.life_spiral_fragment.view

import android.graphics.Path
import android.graphics.Point
import com.example.lifetime.data.AppDataManager
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.base.presenter.BasePresenter
import com.example.lifetime.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject
import kotlin.math.*

class LifeSpiralPresenter<V : LifeSpiralInteractor.LifeSpiralMVPView> @Inject constructor(compositeDisposable: CompositeDisposable,
                                                                                          dataManager: AppDataManager,
                                                                                          schedulerProvider: SchedulerProvider)
    :BasePresenter<V>(dataManager,compositeDisposable,schedulerProvider),
    LifeSpiralInteractor.LifeSpiralMVPPresenter<V> {


    override fun calculateDrawPointList(w: Int, h: Int, person: Person){
        doAsync {
            uiThread {
                getView()?.showLoading()
            }
            val path = Path()
            val p = Point()
            p.x = w / 2
            p.y = h / 2
            val pointList: MutableList<Point> = mutableListOf()
            pointList.add(Point(p.x, p.y))

            val lifeExceptionYears = person.lifeExpectancyYears

            val radius= if(w <= h) (w /2).toDouble() else (h / 2).toDouble()
            val dotRadius =
                sqrt(0.4 * radius.pow(2.0) / (person.lifeExpectancyYears * 365 / 7 + person.lifeExpectancyYears / (4 * 7))).toInt()

            val lastCirclePoint = Point(w / 2, h / 2)
            val maxAngle = getMaxAngle((0.9 * radius).toInt(),lifeExceptionYears.toInt(), dotRadius)

            val range = 1..maxAngle * 5
            var count = 0
            for (angle in range) {
                val a = angle.toDouble() / 5.0
                val r = (a / (maxAngle)) * (0.9 * radius)
                val newX = w / 2f + r * cos(PI * a / 180)
                val newY = h / 2f - r * sin(PI * a / 180)
                path.quadTo(p.x.toFloat(), p.y.toFloat(), newX.toFloat(), newY.toFloat())
                p.y = newY.toInt()
                p.x = newX.toInt()
                if (sqrt(
                        (lastCirclePoint.x.toDouble() - p.x).pow(2.0) +
                                (lastCirclePoint.y.toDouble() - p.y).pow(2.0)
                    ) >= (2 * dotRadius * 1.1)
                ) {

                    lastCirclePoint.x = p.x
                    lastCirclePoint.y = p.y

                    count++
                    if (count <=  ((lifeExceptionYears * 365/7 + lifeExceptionYears/(4*7) ))) {
                        pointList.add(Point(p.x, p.y))
                    }
                }
            }
            uiThread {
                getView()?.setPointList(pointList)
                getView()?.getMainActivity()?.setPointList(pointList)
            }
        }
    }


    private fun getMaxAngle(circleRadius: Int,LifeExpectancyYears: Int, dotRadius: Int): Int {
        var phi = 0
        while (getSpiralLength(phi * PI / 180, 2 * circleRadius).toInt() <= ((LifeExpectancyYears * 365/7 + LifeExpectancyYears/(4*7) ) * dotRadius * 2 * 1.2).toInt()) {
            phi += 1
        }
        return phi
    }

    private fun getSpiralLength(phi: Double, circleRadius: Int): Double =
        ((circleRadius / 4.0 / (phi))) * (ln(sqrt(phi.pow(2.0) + 1) + phi) + phi * sqrt(phi.pow(2.0) + 1))



}