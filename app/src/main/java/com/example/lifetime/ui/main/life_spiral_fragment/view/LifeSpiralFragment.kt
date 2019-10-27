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
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject
import kotlin.math.*

class LifeSpiralFragment : BaseFragment(), LifeSpiralInteractor.LifeSpiralMVPView{


    @Inject
    lateinit var presenter: LifeSpiralInteractor.LifeSpiralMVPPresenter<LifeSpiralInteractor.LifeSpiralMVPView>

    var person: Person? = null
    private var lifeSpiral: LifeSpiral? = null
    private var isDrawed = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_life_spiral, container, false)


    override fun setUp() {
        presenter.onAttach(this)
        view?.setBackgroundColor(Color.TRANSPARENT)
        if (arguments != null) {
            person = arguments?.getSerializable("person") as Person
        } else {
            presenter.getMainPersonFromDataBase()
        }
        lifeSpiral = view?.findViewById(R.id.lifeSpiral)
        Log.d("TAGTAG",isDrawed.toString())

    }

    override fun getMainPerson(person: Person) {
        this.person = person
        lifeSpiral?.viewTreeObserver?.addOnGlobalLayoutListener {
            if(!isDrawed) calculateSpiralPointList()
        }

    }


    override fun onDetach() {
        presenter.onDetach()
        super.onDetach()
    }



    private fun calculateSpiralPointList(){
        if(isDrawed) return

        showLoading()

        isDrawed = true
        lifeSpiral?.visibility = View.INVISIBLE

        doAsync {
            val path = Path()
            val p = Point()
            val w = lifeSpiral?.width!!
            val h = lifeSpiral?.height!!
            Log.d("TAGTAG","$w and $h")
            Log.d("TAGTAG",person.toString())
            p.x = w / 2
            p.y = h / 2
            val pointList: MutableList<Point> = mutableListOf()
            pointList.add(Point(p.x, p.y))

            val lifeExceptionYears = person?.LifeExpectancyYears!!
            val dotRadius = sqrt(0.3 * if(w >= h) w.toDouble().pow(2.0)  else  h.toDouble().pow(2.0)/ (lifeExceptionYears * 52 * 4)).toInt()

            val lastCirclePoint = Point(w / 2, h / 2)
            val maxAngle = getMaxAngle(lifeExceptionYears.toInt(), dotRadius)

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
                hideLoading()
                lifeSpiral?.setParameters(pointList,dotRadius,person)
                lifeSpiral?.visibility = View.VISIBLE
            }
        }

    }

    private fun getMaxAngle(LifeExpectancyYears: Int, dotRadius: Int): Int {
        var phi = 0
        val circleRadius = if (lifeSpiral?.width!! > lifeSpiral?.height!!) lifeSpiral?.height else lifeSpiral?.width
        while (getSpiralLength(phi * PI / 180,circleRadius!!).toInt() <= (LifeExpectancyYears * 52 * dotRadius * 2 * 1.28).toInt()) {
            phi += 1
        }
        return phi
    }

    private fun getSpiralLength(phi: Double, circleRadius: Int): Double =
        ((circleRadius / 4.0 * 0.95 / (phi))) * (ln(sqrt(phi.pow(2.0) + 1) + phi) + phi * sqrt(phi.pow(2.0) + 1))


}
