package com.kurio.carousel.ui.indicators

import android.content.Context
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import com.kurio.carousel.R

abstract class IndicatorShape(context: Context, private var indicatorSize: Int, private var mustAnimateChange: Boolean) : AppCompatImageView(context) {
    private var isChecked = false
    private fun setup() {
        if (indicatorSize == 0) {
            indicatorSize = resources.getDimension(R.dimen.default_indicator_size).toInt()
        }
        val layoutParams = LinearLayout.LayoutParams(indicatorSize, indicatorSize)
        val margin = resources.getDimensionPixelSize(R.dimen.default_indicator_margins)
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        layoutParams.setMargins(margin, 0, margin, 0)
        setLayoutParams(layoutParams)
    }

    open fun onCheckedChange(isChecked: Boolean) {
        if (this.isChecked != isChecked) {
            if (mustAnimateChange) {
                if (isChecked) {
                    scale()
                } else {
                    descale()
                }
            } else {
                if (isChecked) {
                    scale(0)
                } else {
                    descale(0)
                }
            }
            this.isChecked = isChecked
        }
    }

    private fun scale(duration: Int = ANIMATION_DURATION) {
        val scaleAnimation = ScaleAnimation(1f, 1.3f, 1f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = duration.toLong()
        scaleAnimation.fillAfter = true
        startAnimation(scaleAnimation)
    }

    private fun descale(duration: Int = ANIMATION_DURATION) {
        val scaleAnimation = ScaleAnimation(1.3f, 1f, 1.3f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        scaleAnimation.duration = duration.toLong()
        scaleAnimation.fillAfter = true
        startAnimation(scaleAnimation)
    }

    fun setMustAnimateChange(mustAnimateChange: Boolean) {
        this.mustAnimateChange = mustAnimateChange
    }

    companion object {
        const val CIRCLE = 0
        const val SQUARE = 1
        const val ROUND_SQUARE = 2
        const val DASH = 3
        private const val ANIMATION_DURATION = 150
    }

    init {
        setup()
    }
}