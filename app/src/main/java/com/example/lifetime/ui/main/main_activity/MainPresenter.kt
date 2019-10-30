package com.example.lifetime.ui.main.main_activity

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

class MainPresenter<V : MainInteractor.MainMVPView> @Inject internal constructor(
    dataManager: AppDataManager,
    compositeDisposable: CompositeDisposable,
    schedulerProvider: SchedulerProvider
) : BasePresenter<V>(dataManager, compositeDisposable, schedulerProvider),
    MainInteractor.MainMVPPresenter<V> {


    override fun deletePerson(person: Person) = compositeDisposable.add(
        dataManager.deletePerson(person)
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe())

    override fun getPersons(): Boolean =
        compositeDisposable.add(dataManager.loadPersons()
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe {
                getView()?.loadPersons(it.toMutableList())
            })

    override fun getLastPersonFromDb(){
        if (dataManager.getLastPerson() == null) {
            compositeDisposable.add(dataManager.loadPersons()
                .compose(schedulerProvider.ioToMainObservableScheduler())
                .subscribe {
                    it.map { person ->
                        if (person.isMainUser) getView()?.getLastPerson(person)
                    }
                })
        } else {
            getView()?.getLastPerson(dataManager.getLastPerson()!!)
        }

    }

    override fun onButtonClicked() {
        getView()?.openUserDialog(null)
    }

    override fun onPersonClicked(person: Person) {
        getView()?.getPersonFromList(person)
    }

    override fun setLastPersonOnDb(person: Person) {
        dataManager.setLastPerson(person)
    }

    override fun calculateDrawPointList(w: Int, h: Int, person: Person){
        doAsync {
            val path = Path()
            val p = Point()
            p.x = w / 2
            p.y = h / 2
            val pointList: MutableList<Point> = mutableListOf()
            pointList.add(Point(p.x, p.y))

            val lifeExceptionYears = person.LifeExpectancyYears
            val dotRadius = sqrt(0.3 * if(w >= h) w.toDouble().pow(2.0)  else  h.toDouble().pow(2.0)/ (lifeExceptionYears * 52 * 4)).toInt()

            val lastCirclePoint = Point(w / 2, h / 2)
            val maxAngle = getMaxAngle(if (w > h) h else w,lifeExceptionYears.toInt(), dotRadius)

            val range = 1..maxAngle * 10
            var count = 0
            for (angle in range) {
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
                getView()?.setPointList(pointList)
            }
        }
    }

    private fun getMaxAngle(circleRadius: Int,LifeExpectancyYears: Int, dotRadius: Int): Int {
        var phi = 0
        while (getSpiralLength(phi * PI / 180, circleRadius).toInt() <= (LifeExpectancyYears * 52 * dotRadius * 2 * 1.28).toInt()) {
            phi += 1
        }
        return phi
    }

    private fun getSpiralLength(phi: Double, circleRadius: Int): Double =
        ((circleRadius / 4.0 * 0.95 / (phi))) * (ln(sqrt(phi.pow(2.0) + 1) + phi) + phi * sqrt(phi.pow(2.0) + 1))


}