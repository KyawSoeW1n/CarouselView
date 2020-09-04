package com.kurio.carousel.ui.indicators

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.kurio.carousel.R

@SuppressLint("ViewConstructor")
class DashIndicator(context: Context, indicatorSize: Int, mustAnimateChanges: Boolean, indicatorActiveColor: Int,
                    indicatorInactiveColor: Int) : IndicatorShape(context, indicatorSize, mustAnimateChanges) {
    var indicatorActiveColor = 0
    var indicatorInactiveColor = 0
    override fun onCheckedChange(isChecked: Boolean) {
        super.onCheckedChange(isChecked)
        if (isChecked) {
            run {
                val drawable = ResourcesCompat.getDrawable(resources, R.drawable.indicator_dash_selected, null)
                drawable!!.setTint(indicatorActiveColor)
                background = drawable
            }
        } else {
            run {
                val drawable = ResourcesCompat.getDrawable(resources, R.drawable.indicator_dash_unselected, null)
                drawable!!.setTint(indicatorInactiveColor)
                background = drawable
            }
        }
    }

    init {
        this.indicatorActiveColor = indicatorActiveColor
        this.indicatorInactiveColor = indicatorInactiveColor
        run {
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.indicator_dash_unselected, null)
            drawable!!.setTint(indicatorInactiveColor)
            background = drawable
        }
        val layoutParams = layoutParams as LinearLayout.LayoutParams
        layoutParams.width = resources.getDimensionPixelSize(R.dimen.default_dash_indicator_width)
        layoutParams.height = resources.getDimensionPixelSize(R.dimen.default_dash_indicator_height)
        setLayoutParams(layoutParams)
    }
}