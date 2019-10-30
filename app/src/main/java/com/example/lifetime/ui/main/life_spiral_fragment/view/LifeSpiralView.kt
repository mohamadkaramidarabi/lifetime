package com.example.lifetime.ui.main.life_spiral_fragment.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.util.CommonUtil
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlin.math.*


class LifeSpiralView (context: Context, attributeSet: AttributeSet? = null) : View(context,attributeSet){


    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var person: Person? = null

    init {
        setToDefaultsValues()
    }

    private lateinit var pointList: List<Point>
    private var dotRadius: Int? = null

    fun setPerson(person: Person?) {
        this.person = person
    }

    var isDrawed = false


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        calculateSpiralPointList(canvas)
    }
    private fun setToDefaultsValues() {
        paint.color = Color.WHITE
        isDrawed = false

    }
    fun reDraw() {
        setToDefaultsValues()
    }
    private fun drawSpiral(canvas: Canvas?) {
        if(person ==null) return
        if(isDrawed) return
        isDrawed = true
        canvas?.drawColor(Color.TRANSPARENT)

        for ((i, point) in pointList.withIndex()) {
            if (i > CommonUtil.calculateAge(PersianCalendar(person!!.birthDate)) / 7) {
                paint.color = Color.GRAY
                paint.strokeWidth = dotRadius!! / 2f
            }
            drawCircle(
                point.x.toFloat(),
                point.y.toFloat(),
                dotRadius!!.toFloat(),
                paint,
                canvas
            )
        }
    }
    var w: Int? = null
    var h: Int? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

    }

    private fun calculateSpiralPointList(canvas: Canvas?){
        if(isDrawed) return
        val path = Path()
        val p = Point()
        p.x = w!! / 2
        p.y = h!! / 2
        val pointList: MutableList<Point> = mutableListOf()
        pointList.add(Point(p.x, p.y))

        val lifeExceptionYears = person?.LifeExpectancyYears!!
        val dotRadius = sqrt(0.3 * if(w!! >= h!!) w!!.toDouble().pow(2.0)  else  h!!.toDouble().pow(2.0)/ (lifeExceptionYears * 52 * 4)).toInt()

        val lastCirclePoint = Point(w!! / 2, h!! / 2)
        val maxAngle = getMaxAngle(lifeExceptionYears.toInt(), dotRadius)

        val range = 1..maxAngle * 10
        var count = 0
        for (angle in range) {
            val a = angle.toDouble() / 10.0
            val r = (a / (maxAngle)) * (w!! / 2.0 * 0.95)
            val newX = w!! / 2f + r * cos(PI * a / 180)
            val newY = h!! / 2f - r * sin(PI * a / 180)
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

        this@LifeSpiralView.pointList = pointList
        this@LifeSpiralView.dotRadius = dotRadius
        drawSpiral(canvas)
    }

    private fun getMaxAngle(LifeExpectancyYears: Int, dotRadius: Int): Int {
        var phi = 0
        val circleRadius = if (w!! > h!!) h else w
        while (getSpiralLength(phi * PI / 180,circleRadius!!).toInt() <= (LifeExpectancyYears * 52 * dotRadius * 2 * 1.28).toInt()) {
            phi += 1
        }
        return phi
    }

    private fun getSpiralLength(phi: Double, circleRadius: Int): Double =
        ((circleRadius / 4.0 * 0.95 / (phi))) * (ln(sqrt(phi.pow(2.0) + 1) + phi) + phi * sqrt(phi.pow(2.0) + 1))




    private fun drawCircle(
        centerX: Float,
        centerY: Float,
        radius: Float,
        paint: Paint,
        canvas: Canvas?
    ) {
        canvas?.drawCircle(
            centerX,
            centerY,
            radius,
            paint
        )
    }
}
