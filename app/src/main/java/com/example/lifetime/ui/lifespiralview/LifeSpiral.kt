package com.example.lifetime.ui.lifespiralview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import com.example.lifetime.BaseApplication
import com.example.lifetime.data.database.repository.person.Person
import com.example.lifetime.ui.main.view.MainActivity
import com.example.lifetime.util.SchedulerProvider
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Math.pow
import java.util.function.Consumer
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.*

@Suppress("NAME_SHADOWING")
class LifeSpiral (context: Context,attributeSet: AttributeSet) : SurfaceView(context,attributeSet),
    SurfaceHolder.Callback {

    private val paint: Paint
    private val backgroundPaint: Paint
    private var w: Int = 0
    private var h = 0
    private val path = Path()
    private var surfaceHolder: SurfaceHolder?= null

    var ageByDay = 30 * 365



    init {
        holder.addCallback(this)
        backgroundPaint = Paint()
        backgroundPaint.color = Color.WHITE
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }
    

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        this.surfaceHolder = holder
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        this.surfaceHolder = holder!!
        drawSpiral(holder)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.w = w
        this.h = h
    }

    private fun drawSpiral(holder: SurfaceHolder?) {
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
        path.moveTo(w / 2f, h / 2f)
        val p = Point()
        p.x = w / 2
        p.y = h / 2
        val lifeExceptionYears = 5
        val dotRadius = sqrt(0.3 * pow(w.toDouble(), 2.0) / (lifeExceptionYears * 52 * 4)).toInt()

        val lastCirclePoint = Point(w / 2, h / 2)
        val maxAngle = getMaxAngle(lifeExceptionYears, dotRadius)
        var count = 0

        val range = 1..maxAngle * 10
        val observable: Observable<Unit> = Observable.create { emitter ->
            emitter.onNext(Unit)
        }
        observable.subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .doOnNext {
                val canvas = holder?.lockCanvas(null)
                canvas?.drawRect(0f, 0f, w.toFloat(), h.toFloat(), backgroundPaint)
                holder?.unlockCanvasAndPost(canvas)
            }
            .doOnNext {
                Thread.sleep(5000)
                Log.d("TAG",Thread.currentThread().name)
                val canvas = holder?.lockCanvas(null)
                canvas?.drawRect(0f, 0f, w.toFloat(), h.toFloat(), backgroundPaint)
                canvas?.drawRect(0f, 0f, w.toFloat(), h.toFloat(), backgroundPaint)
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
                        if (count > ageByDay / 7) {
                            paint.color = Color.RED
                            paint.style = Paint.Style.STROKE
                        }
                        drawCircle(
                            p.x.toFloat(),
                            p.y.toFloat(),
                            dotRadius.toFloat(),
                            paint,
                            canvas
                        )
                        if (count > lifeExceptionYears * 365 / 7) break
                    }
                }
                holder?.unlockCanvasAndPost(canvas)
            }
            .subscribe()

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

    fun reDraw(ageByDay: Int) {
        this.ageByDay = ageByDay
        drawSpiral(this.surfaceHolder)
    }
}
