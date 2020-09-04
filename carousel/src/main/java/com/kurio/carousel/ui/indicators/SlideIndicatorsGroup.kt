package com.kurio.carousel.ui.indicators


import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.kurio.carousel.R
import com.kurio.carousel.listener.OnSlideChangeListener
import java.util.*
import kotlin.properties.Delegates

class SlideIndicatorsGroup(context: Context) : LinearLayout(context), OnSlideChangeListener {


    private var slidesCount = 0
    private val indicatorShapes: MutableList<IndicatorShape> = ArrayList()
    private var defaultIndicator by Delegates.notNull<Int>()
    private var indicatorSize by Delegates.notNull<Int>()
    private var animateIndicators by Delegates.notNull<Boolean>()
    private var indicatorActiveColor by Delegates.notNull<Int>()
    private var indicatorInactiveColor by Delegates.notNull<Int>()

    constructor(context: Context,
                defaultIndicator: Int, indicatorSize: Int, animateIndicators: Boolean,
                indicatorActiveColor: Int, indicatorInactiveColor: Int) : this(context) {
        this.defaultIndicator = defaultIndicator
        this.indicatorSize = indicatorSize
        this.animateIndicators = animateIndicators
        this.indicatorActiveColor = indicatorActiveColor
        this.indicatorInactiveColor = indicatorInactiveColor
        setup()
    }

    fun setSlides(slidesCount: Int) {
        removeAllViews()
        indicatorShapes.clear()
        this.slidesCount = 0
        for (i in 0 until slidesCount) {
            onSlideAdd()
        }
        this.slidesCount = slidesCount
    }

    private fun onSlideAdd() {
        slidesCount += 1
        addIndicator()
    }

    private fun addIndicator() {
        when (defaultIndicator) {
            IndicatorShape.SQUARE -> {
                val indicator = SquareIndicator(context, indicatorSize, animateIndicators, indicatorActiveColor, indicatorInactiveColor)
                indicatorShapes.add(indicator)
                addView(indicator)
            }
            IndicatorShape.ROUND_SQUARE -> {
                val indicator = RoundSquareIndicator(context, indicatorSize, animateIndicators, indicatorActiveColor, indicatorInactiveColor)
                indicatorShapes.add(indicator)
                addView(indicator)
            }
            IndicatorShape.DASH -> {
                val indicator = DashIndicator(context, indicatorSize, animateIndicators, indicatorActiveColor, indicatorInactiveColor)
                indicatorShapes.add(indicator)
                addView(indicator)
            }
            IndicatorShape.CIRCLE -> {
                val indicator = CircleIndicator(context, indicatorSize, animateIndicators, indicatorActiveColor, indicatorInactiveColor)
                indicatorShapes.add(indicator)
                addView(indicator)
            }
        }
    }

    private fun setup() {
        orientation = HORIZONTAL
        val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, indicatorSize * 2)
        layoutParams.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        val margin = resources.getDimensionPixelSize(R.dimen.default_indicator_margins) * 2
        layoutParams.setMargins(0, 0, 0, margin)
        setLayoutParams(layoutParams)
    }

    override fun onSlideChange(selectedSlidePosition: Int) {
        for (i in indicatorShapes.indices) {
            if (i == selectedSlidePosition) {
                indicatorShapes[i].onCheckedChange(true)
            } else {
                indicatorShapes[i].onCheckedChange(false)
            }
        }
    }

    fun setMustAnimateIndicators(mustAnimateIndicators: Boolean) {
        this.animateIndicators = mustAnimateIndicators
        for (indicatorShape in indicatorShapes) {
            indicatorShape.setMustAnimateChange(mustAnimateIndicators)
        }
    }
}