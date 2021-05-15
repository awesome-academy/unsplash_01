package com.sun.unsplash_01.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.sun.unsplash_01.extensions.toBitmap


class CustomImageFilterView : ImageFilterView {

    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initPaint()
    }

    private fun initPaint() {
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = STROKE_WIDTH
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.toBitmap()?.let { canvas?.drawBitmap(it, 0F, 0F, null) }
        canvas?.drawPath(path, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y

        if (isDraw) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    x?.let { xData ->
                        y?.let { yData ->
                            path.moveTo(xData, yData)
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    x?.let { xData ->
                        y?.let { yData ->
                            path.lineTo(xData, yData)
                        }
                    }
                }
            }
            invalidate()
        }
        return true
    }

    companion object {
        var isDraw = false
        private const val STROKE_WIDTH = 10F
    }
}
