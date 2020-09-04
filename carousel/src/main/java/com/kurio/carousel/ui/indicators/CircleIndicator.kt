package com.kurio.carousel.ui.indicators

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.res.ResourcesCompat
import com.kurio.carousel.R

@SuppressLint("ViewConstructor")
class CircleIndicator(context: Context, indicatorSize: Int, mustAnimateChanges: Boolean,
                      indicatorActiveColor: Int, indicatorInactiveColor: Int) : IndicatorShape(context, indicatorSize, mustAnimateChanges) {
    var indicatorActiveColor = 0
    var indicatorInactiveColor = 0
    override fun onCheckedChange(isChecked: Boolean) {
        super.onCheckedChange(isChecked)
        if (isChecked) {
            run {
                val drawable = ResourcesCompat.getDrawable(resources, R.drawable.indicator_circle_selected, null)
                drawable!!.setTint(indicatorActiveColor)
                background = drawable
            }
        } else {
            run {
                val drawable = ResourcesCompat.getDrawable(resources, R.drawable.indicator_circle_unselected, null)
                drawable!!.setTint(indicatorInactiveColor)
                background = drawable
            }
        }
    }

    init {
        this.indicatorActiveColor = indicatorActiveColor
        this.indicatorInactiveColor = indicatorInactiveColor
        run {
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.indicator_circle_unselected, null)!!
            drawable.setTint(indicatorInactiveColor)
            background = drawable
        }
    }
}