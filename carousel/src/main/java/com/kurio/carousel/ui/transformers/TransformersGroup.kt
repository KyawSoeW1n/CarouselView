package com.kurio.carousel.ui.transformers

import android.content.Context
import androidx.viewpager.widget.ViewPager.PageTransformer
import com.kurio.carousel.model.EffectObject

class TransformersGroup {
    fun getTransformer(context: Context, transformer: Int): PageTransformer {
        return when (transformer) {
            EffectObject.ANTI_CLOCK_SPIN -> AntiClockSpinTransformer()
            EffectObject.CAROUSEL_EFFECT -> CarouselEffectTransformer(context)
            EffectObject.CLOCK_SPIN -> ClockSpinTransformer()
            EffectObject.CUBE_IN_ROTATION -> CubeInRotationTransformer()
            EffectObject.CUBE_OUT_ROTATION -> CubeOutRotationTransformer()
            EffectObject.DEPTH -> DepthTransformer()
            EffectObject.FADE_OUT -> FadeOutTransformer()
            EffectObject.HORIZONTAL_FLIP -> HorizontalFlipTransformer()
            EffectObject.SIMPLE -> SimpleTransformer()
            EffectObject.SPINNER -> SpinnerTransformer()
            EffectObject.VERTICAL_FLIP -> VerticalFlipTransformer()
            EffectObject.VERTICAL_SHUT -> VerticalShutTransformer()
            else -> ZoomOutTransformer()
        }
    }
}