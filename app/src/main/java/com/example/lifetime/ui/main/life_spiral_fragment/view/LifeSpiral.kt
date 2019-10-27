package com.example.lifetime.ui.main.life_spiral_fragment.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceView
import android.view.View
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.util.CommonUtil
import io.reactivex.disposables.CompositeDisposable
import ir.hamsaa.persiandatepicker.util.PersianCalendar


class LifeSpiral (context: Context,attributeSet: AttributeSet? = null) : View(context,attributeSet){


    private val compositeDisposable = CompositeDisposable()
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var person: Person? = null

    init {
        paint.color = Color.WHITE
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawSpiral(canvas)
        Log.d("lifeSpiral","OnDraw")
    }

    lateinit var pointList: List<Point>
    var dotRadius: Int? = null
    fun setParameters(pointList: List<Point>,dotRadius: Int,person: Person?) {
        this.pointList = pointList
        this.dotRadius =dotRadius
        this.person = person
    }



    override fun onDetachedFromWindow() {
        compositeDisposable.clear()
        super.onDetachedFromWindow()
    }


    var isDrawed = false
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
