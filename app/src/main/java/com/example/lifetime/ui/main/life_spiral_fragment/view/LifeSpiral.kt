package com.example.lifetime.ui.main.life_spiral_fragment.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.util.CommonUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlin.math.*

class LifeSpiral (context: Context,attributeSet: AttributeSet? = null) : View(context,attributeSet){

    private val paint: Paint
    private val backgroundPaint: Paint = Paint()
    private var w: Int = 0
    private var h = 0
    private val path = Path()
    private var surfaceHolder: SurfaceHolder?= null

    val person: Person
    var finishDraw: PublishSubject<Boolean> = PublishSubject.create()
    private  var canvas: Canvas? = null


    init {
        backgroundPaint.color = Color.TRANSPARENT
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        person = Person("fake name", 80f, PersianCalendar().time.time)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.canvas =canvas
        drawSpiral()
    }
    


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
    }

    private fun drawSpiral() {
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        path.moveTo(w / 2f, h / 2f)
        val p = Point()
        p.x = w / 2
        p.y = h / 2
        val lifeExceptionYears = person.LifeExpectancyYears
        val dotRadius = sqrt(0.3 * w.toDouble().pow(2.0) / (lifeExceptionYears * 52 * 4)).toInt()

        val lastCirclePoint = Point(w / 2, h / 2)
        val maxAngle = getMaxAngle(lifeExceptionYears.toInt(), dotRadius)
        var count = 0

        val range = 1..maxAngle * 10

        canvas?.drawRect(0f, 0f, w.toFloat(), h.toFloat(), backgroundPaint)
        canvas?.drawColor(Color.TRANSPARENT)
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

                count++
                lastCirclePoint.x = p.x
                lastCirclePoint.y = p.y
                if (count > CommonUtil.calculateAge(PersianCalendar(person.birthDate)) / 7) {
                    paint.color = Color.GRAY
                    paint.strokeWidth = dotRadius/2f
                }
                drawCircle(
                    p.x.toFloat(),
                    p.y.toFloat(),
                    dotRadius.toFloat(),
                    paint,
                    canvas
                )
                if (count > lifeExceptionYears * 365 / 7) {
                    break
                }
            }
        }

    }

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

    private fun getMaxAngle(LifeExpectancyYears: Int, dotRadius: Int): Int {
        var phi = 0
        while (getSpiralLength(phi * PI / 180).toInt() <= (LifeExpectancyYears * 52 * dotRadius * 2 * 1.28).toInt()) {
            phi += 1
        }
        return phi
    }

    private fun getSpiralLength(phi: Double): Double =
        ((w / 4.0 * 0.95 / (phi))) * (ln(sqrt(phi.pow(2.0) + 1) + phi) + phi * sqrt(phi.pow(2.0) + 1))

    fun reDraw(person: Person) {
        this.person.let {
            it.LifeExpectancyYears=person.LifeExpectancyYears
            it.birthDate=person.birthDate
            it.name = person.name
            it.id = person.id
        }
        if (canvas != null) {
            drawSpiral()
        }
    }
}
