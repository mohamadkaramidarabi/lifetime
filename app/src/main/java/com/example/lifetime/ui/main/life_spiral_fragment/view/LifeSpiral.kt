package com.example.lifetime.ui.main.life_spiral_fragment.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.util.CommonUtil
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import org.jetbrains.anko.doAsync
import kotlin.math.*


class LifeSpiral (context: Context,attributeSet: AttributeSet? = null) : SurfaceView(context,attributeSet){


    private val compositeDisposable = CompositeDisposable()
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var w: Int = 0
    private var h = 0
    private val path = Path()
    val person: Person = Person("fake name", 80f, PersianCalendar(1000000000000).time.time)

    init {
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        drawSpiral()
    }


    override fun onDetachedFromWindow() {
        compositeDisposable.clear()
        super.onDetachedFromWindow()
    }




    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawSpiral()
        this.w = w
        this.h = h
    }

    var isDrawed = false
    private fun drawSpiral() {
        if(isDrawed) return
        isDrawed = true
        doAsync {
            Thread.sleep(1000)
            paint.color = Color.WHITE
            paint.style = Paint.Style.FILL
            path.moveTo(w / 2f, h / 2f)
            val p = Point()
            p.x = w / 2
            p.y = h / 2
            val pointList: MutableList<Point> = mutableListOf()
            pointList.add(Point(p.x, p.y))

            val lifeExceptionYears = person.LifeExpectancyYears
            val dotRadius = sqrt(0.3 * if(w>=h) w.toDouble().pow(2.0)  else  h.toDouble().pow(2.0)/ (lifeExceptionYears * 52 * 4)).toInt()

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

            val canvas = holder.lockCanvas(null)
            for ((i, point) in pointList.withIndex()) {
                if (i > CommonUtil.calculateAge(PersianCalendar(person.birthDate)) / 7) {
                    paint.color = Color.GRAY
                    paint.strokeWidth = dotRadius / 2f
                }
                drawCircle(
                    point.x.toFloat(),
                    point.y.toFloat(),
                    dotRadius.toFloat(),
                    paint,
                    canvas
                )

            }
            holder.unlockCanvasAndPost(canvas)
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
        isDrawed = false
        this.person.let {
            it.LifeExpectancyYears=person.LifeExpectancyYears
            it.birthDate=person.birthDate
            it.name = person.name
            it.id = person.id
        }
        if (holder != null) {
            drawSpiral()
        }
    }
}
