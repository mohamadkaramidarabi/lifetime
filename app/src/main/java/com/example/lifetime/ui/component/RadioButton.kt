package com.example.lifetime.ui.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.lifetime.util.AndroidUtilities

class RadioButton(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attributeSet, defStyleAttr) {

    private val TAG = "RadioButton"
    private var bitmap: Bitmap? = null
    private var bitmapCanvas: Canvas? = null

    private var checkedColor: Int = 0
    private var color: Int = 0

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

    fun setColor(color: Int, checkedColor: Int) {
        this.color = color
        this.checkedColor = checkedColor
    }

    override fun setBackgroundColor(color: Int) {
        this.color = color
        invalidate()
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

        } else {



        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (bitmap == null || bitmap!!.width != measuredWidth) {
            if (bitmap != null) {
                bitmap!!.recycle()
                bitmap = null
            }
        }

    }

}