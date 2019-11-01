package com.example.lifetime.ui.main.life_spiral_fragment.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.util.CommonUtil
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import kotlin.math.*


class LifeSpiralView (context: Context, attributeSet: AttributeSet? = null) : View(context,attributeSet){


    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var person: Person? = null

    private var pointList: List<Point>? = null
    private var forClear = false

    var w: Int? = null
        private set
    var h: Int? = null
        private set

    init {
        setToDefaultsValues()
    }
    private fun setToDefaultsValues() {
        person = null
        pointList = null
        paint.color = Color.WHITE
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h

    }

    fun setParameters(pointList: List<Point>, person: Person) {
        setToDefaultsValues()
        this.pointList = pointList
        this.person = person
        invalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSpiral(canvas)
        setToDefaultsValues()
        forClear = false
    }

    fun clear() {
        forClear = true
        setToDefaultsValues()
        invalidate()
    }


    private fun drawSpiral(canvas: Canvas?) {
        if (forClear) return
        if (person == null || pointList == null) return
        val radius= if(w!!<=h!!) (w!!/2).toDouble() else (h!!).toDouble() / 2
        val dotRadius =
            sqrt(0.4 * radius.pow(2.0) / (person?.LifeExpectancyYears!! * 52)).toInt()
        for ((i, point) in pointList!!.withIndex()) {
            if (i > CommonUtil.calculateAge(PersianCalendar(person!!.birthDate)) / 7) {
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
}
