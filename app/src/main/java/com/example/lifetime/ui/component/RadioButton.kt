package com.example.lifetime.ui.component

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import com.example.lifetime.util.AndroidUtilities

class RadioButton @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attributeSet, defStyleAttr) {

    private val TAG = "RadioButton"
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null

    private var checkedColor: Int = 0
    private var color: Int = 0

    var progress: Float = 0.toFloat()
        @Keep
        set(value) {
            if (this.progress == value) return
            field = value
            invalidate()
        }
    private var checkAnimator: ObjectAnimator? = null

    private var attachedToWindow: Boolean = false
    var isChecked: Boolean = false
        private set
    private var size = AndroidUtilities.dp(24f)

    companion object {
        private var paint: Paint? = null
        private var eraser: Paint? = null
        private var checkedPaint: Paint? = null
    }

    init {
        if (paint == null) {
            paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint!!.strokeWidth = AndroidUtilities.dp(2f).toFloat()
            paint!!.style = Paint.Style.STROKE

            checkedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            eraser = Paint(Paint.ANTI_ALIAS_FLAG)
            eraser!!.color = 0
            eraser!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

        try {
            bitmap = Bitmap.createBitmap(
                AndroidUtilities.dp(size.toFloat()),
                AndroidUtilities.dp(size.toFloat()),
                Bitmap.Config.ARGB_4444
            )
            bitmapCanvas = Canvas(bitmap!!)
        } catch (e: Throwable) {
            e.printStackTrace()
        }


    }
    fun setSize(value: Int) {
        if (size == value) {
            return
        }
        size = value
    }

    fun setColor(color: Int, checkedColor: Int) {
        this.color = color
        this.checkedColor = checkedColor
        invalidate()
    }

    override fun setBackgroundColor(color: Int) {
        this.color = color
        invalidate()
    }

    fun setCheckedColor(checkedColor: Int) {
        this.checkedColor = checkedColor
        invalidate()
    }

    private fun cancelCheckAnimator() {
        if(checkAnimator != null) checkAnimator!!.cancel()
    }

    private fun animateToCheckedState(newCheckedState: Boolean) {
        checkAnimator = ObjectAnimator.ofFloat(this,"progress",if(newCheckedState) 1f else 0f)
        checkAnimator!!.duration = 200
        checkAnimator!!.start()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        attachedToWindow = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        attachedToWindow = false
    }

    fun setChecked(checked: Boolean, animated: Boolean) {
        if (checked == isChecked) {
            return
        }
        isChecked = checked

        if (attachedToWindow && animated) {
            animateToCheckedState(checked)
        } else {
            cancelCheckAnimator()
            progress = if(checked) 1.0f else 0.0f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (bitmap == null || bitmap!!.width != measuredWidth) {
            if (bitmap != null) {
                bitmap!!.recycle()
                bitmap = null
            }
            try {
                bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
                bitmapCanvas = Canvas(bitmap)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        val circleProgress: Float
        if (this.progress <= 0.5f) {
            paint!!.color = color
            checkedPaint!!.color = color
            circleProgress = this.progress / 0.5f
        } else {
            circleProgress = 2.0f - this.progress / 0.5f
            val r1 = Color.red(color)
            val rD = ((Color.red(checkedColor) - r1) * (1.0f - circleProgress)).toInt()
            val g1 = Color.green(color)
            val gD = ((Color.green(checkedColor) - g1) * (1.0f - circleProgress)).toInt()
            val b1 = Color.blue(color)
            val bD = ((Color.blue(checkedColor) - b1) * (1.0f - circleProgress)).toInt()
            val c = Color.rgb(r1 + rD, g1 + gD, b1 + bD)
            paint!!.color = c
            checkedPaint!!.color = c
        }
        if (bitmap != null) {
            bitmap!!.eraseColor(0)
            val radius = size / 2 - (1 + circleProgress) * AndroidUtilities.density
            bitmapCanvas!!.drawCircle(
                (measuredWidth / 2).toFloat(),
                (measuredHeight / 2).toFloat(),
                radius,
                paint!!
            )
            if (this.progress <= 0.5f) {
                bitmapCanvas!!.drawCircle(
                    (measuredWidth / 2).toFloat(),
                    (measuredHeight / 2).toFloat(),
                    radius - AndroidUtilities.dp(1f),
                    checkedPaint!!
                )
                bitmapCanvas!!.drawCircle(
                    (measuredWidth / 2).toFloat(),
                    (measuredHeight / 2).toFloat(),
                    (radius - AndroidUtilities.dp(1f)) * (1.0f - circleProgress),
                    eraser!!
                )
            } else {
                bitmapCanvas!!.drawCircle(
                    (measuredWidth / 2).toFloat(),
                    (measuredHeight / 2).toFloat(),
                    size / 4 + (radius - AndroidUtilities.dp(1f).toFloat() - (size / 4).toFloat()) * circleProgress,
                    checkedPaint!!
                )
            }
            canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        }

    }

}