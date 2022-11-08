package com.jisoo.customviewcodelabs

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import java.lang.Integer.min
import java.lang.Math.cos
import java.lang.Math.sin

class DialView @JvmOverloads constructor(context: Context, attrs:AttributeSet?=null, defStyleAttr: Int = 0)
    : View(context,attrs,defStyleAttr) {

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.DialView) {
            fanSpeedLowColor = getColor(R.styleable.DialView_fanColor1,0)
            fanSpeedMediumColor = getColor(R.styleable.DialView_fanColor2,0)
            fanSpeedMaxColor = getColor(R.styleable.DialView_fanColor3,0)
        }
    }

    private var fanSpeedLowColor = 0
    private var fanSpeedMediumColor = 0
    private var fanSpeedMaxColor = 0

    private var radius = 0.0f //현재 원의 반지름
    private var fanSpeed = FanSpeed.OFF //기본 값 0ff
    private val pointPosition: PointF = PointF(0.0f, 0.0f) //화면에 뷰의 여러 요소를 그리는 데 사용되는 X,Y

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("",Typeface.BOLD)
    }

    override fun performClick(): Boolean {
        if(super.performClick()) return true

        fanSpeed = fanSpeed.next()
        contentDescription = resources.getString(fanSpeed.label)

        invalidate() //뷰 무효화
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(width,height) / 2.0 * 0.8).toFloat()
    }

    private fun PointF.computeXYForSpeed(pos: FanSpeed, radius:Float) {
        val startAngle = Math.PI * (9 / 8.0)
        val angle = startAngle + pos.ordinal * (Math.PI / 4)
        x = (radius * cos(angle)).toFloat() + width / 2
        y = (radius * sin(angle)).toFloat() + height / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = when(fanSpeed) {
            FanSpeed.OFF -> Color.GRAY
            FanSpeed.LOW -> fanSpeedLowColor
            FanSpeed.MEDIUM -> fanSpeedMediumColor
            FanSpeed.HIGH -> fanSpeedMaxColor
        }
//        paint.color = if(fanSpeed == FanSpeed.OFF) Color.GRAY else Color.GREEN
        canvas.drawCircle((width/2).toFloat(), (height/2).toFloat(), radius, paint)

        val marketRadius = radius + RADIUS_OFFSET_INDICATOR
        pointPosition.computeXYForSpeed(fanSpeed,marketRadius)
        paint.color = Color.BLACK
        canvas.drawCircle(pointPosition.x, pointPosition.y,  radius/12 , paint)

        val labelRadius = radius + RADIUS_OFFSET_LABEL
        for(i in FanSpeed.values()) {
            pointPosition.computeXYForSpeed(i,labelRadius)
            val label = resources.getString(i.label)
            canvas.drawText(label,pointPosition.x,pointPosition.y,paint)
        }



    }

}

