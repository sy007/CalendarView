package com.sy007.calendar.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.sy007.calendar.R
import com.sy007.calendar.utils.Util

/**
 * 周视图
 * @author sy007
 * @date 2019/1/20
 */
class WeekView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private val weekendTextColor: Int
    private val textColor: Int
    private val textSize: Float
    private val columnNum = 7
    private var labelWidth = 0f
    private val weekContent: Array<CharSequence>?
    private var weekIndexToColor: MutableMap<Int, Int> = mutableMapOf()
    private lateinit var labelRectF: RectF
    private lateinit var fontMetrics: Paint.FontMetrics
    private lateinit var labelPaint: Paint

    init {
        val defaultLabelTextSize = Util.sp2px(context, 13f)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeekView)
        weekendTextColor = typedArray.getColor(R.styleable.WeekView_wv_weekend_text_color, Color.parseColor("#FF6E00"))
        textColor = typedArray.getColor(R.styleable.WeekView_wv_text_color, Color.parseColor("#000000"))
        textSize = typedArray.getDimension(R.styleable.WeekView_wv_text_size, defaultLabelTextSize)
        weekContent = typedArray.getTextArray(R.styleable.WeekView_wv_content)
        weekContent?.forEachIndexed { index, _ ->
            weekIndexToColor[index] = textColor
        }
        val weekendIndex = typedArray.getTextArray(R.styleable.WeekView_wv_weekend_index)
        weekendIndex?.forEach {
            weekIndexToColor[it.toString().toInt()] = weekendTextColor
        }
        typedArray.recycle()
        initPaint()
    }

    private fun initPaint() {
        labelRectF = RectF()
        labelPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        labelPaint.textAlign = Paint.Align.CENTER
        labelPaint.textSize = textSize
        fontMetrics = Paint.FontMetrics()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        labelPaint.getFontMetrics(fontMetrics)
        setMeasuredDimension(widthMeasureSpec, (fontMetrics.descent - fontMetrics.ascent).toInt() + paddingTop + paddingBottom)
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        labelWidth = (w - paddingLeft - paddingRight) / (columnNum * 1.0f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (weekContent.isNullOrEmpty()) {
            return
        }
        labelPaint.getFontMetrics(fontMetrics)
        var left = paddingLeft
        val top = paddingTop
        val labelHeight = height - paddingTop - paddingBottom
        for (i in 0 until columnNum) {
            labelRectF[left.toFloat(), top.toFloat(), left + labelWidth] = (top + labelHeight).toFloat()
            labelPaint.color = weekIndexToColor[i]!!
            val distance = (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
            val baseline = labelRectF.centerY() + distance
            canvas.drawText(weekContent[i].toString(), labelRectF.centerX(), baseline, labelPaint)
            left += labelWidth.toInt()
        }
    }
}