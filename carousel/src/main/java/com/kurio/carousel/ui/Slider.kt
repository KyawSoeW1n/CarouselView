package com.kurio.carousel.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager.widget.ViewPager.PageTransformer
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.kurio.carousel.R
import com.kurio.carousel.model.Slide
import com.kurio.carousel.ui.transformers.TransformersGroup
import com.kurio.carousel.ui.adapter.SliderAdapter
import com.kurio.carousel.ui.customUI.LooperWrapViewPager
import com.kurio.carousel.ui.indicators.IndicatorShape
import com.kurio.carousel.ui.indicators.SlideIndicatorsGroup

class Slider : FrameLayout, OnPageChangeListener {
    private var viewPager: LooperWrapViewPager? = null
    private var itemClickListener: OnItemClickListener? = null

    //Custom attributes
    private var sliderPlaceHolderImage: Drawable? = null
    private var sliderErrorImage: Drawable? = null
    private var defaultIndicator = 0
    private var indicatorSize = 0
    private var defaultTransition = 0
    private var sliderImageViewHeight = 0
    private var mustAnimateIndicators = false
    private var mustLoopSlides = false
    private var slideIndicatorsGroup: SlideIndicatorsGroup? = null
    private var slideShowInterval = 5000
    private val sliderHandler = Handler(Looper.getMainLooper())
    private var slideCount = 0
    private var currentPageNumber = 0
    private var pageMargin = 0
    private var sliderPaddingTop = 0
    private var sliderPaddingLeft = 0
    private var sliderPaddingRight = 0
    private var sliderPaddingBottom = 0
    private var hideIndicators = false
    private var sliderTransformation = 0
    private var indicatorActiveColor = 0
    private var indicatorInactiveColor = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        parseCustomAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        parseCustomAttributes(attrs)
    }

    private fun parseCustomAttributes(attributeSet: AttributeSet?) {
        try {
            if (attributeSet != null) {
                val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BannerSlider)
                try {
                    indicatorSize = typedArray.getDimensionPixelSize(R.styleable.BannerSlider_indicatorSize, resources.getDimensionPixelSize(R.dimen.default_indicator_size))
                    pageMargin = typedArray.getDimensionPixelSize(R.styleable.BannerSlider_pageMargin, resources.getDimensionPixelSize(R.dimen.default_page_margin))
                    sliderPaddingTop = typedArray.getDimensionPixelSize(R.styleable.BannerSlider_paddingTop, resources.getDimensionPixelSize(R.dimen.default_padding))
                    sliderPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.BannerSlider_paddingLeft, resources.getDimensionPixelSize(R.dimen.default_padding))
                    sliderPaddingRight = typedArray.getDimensionPixelSize(R.styleable.BannerSlider_paddingRight, resources.getDimensionPixelSize(R.dimen.default_padding))
                    sliderPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.BannerSlider_paddingBottom, resources.getDimensionPixelSize(R.dimen.default_padding))
                    sliderPlaceHolderImage = typedArray.getDrawable(R.styleable.BannerSlider_sliderPlaceholderImage)
                    sliderErrorImage = typedArray.getDrawable(R.styleable.BannerSlider_sliderErrorImage)
                    defaultIndicator = typedArray.getInt(R.styleable.BannerSlider_defaultIndicators, IndicatorShape.CIRCLE)
                    mustAnimateIndicators = typedArray.getBoolean(R.styleable.BannerSlider_animateIndicators, true)
                    mustLoopSlides = typedArray.getBoolean(R.styleable.BannerSlider_loopSlides, false)
                    hideIndicators = typedArray.getBoolean(R.styleable.BannerSlider_hideIndicators, false)
                    val slideShowIntervalSecond = typedArray.getInt(R.styleable.BannerSlider_intervalSecond, 5)
                    defaultTransition = typedArray.getInt(R.styleable.BannerSlider_defaultTransition, 9)
                    sliderTransformation = typedArray.getInt(R.styleable.BannerSlider_defaultTransform, 1)
                    slideShowInterval = slideShowIntervalSecond * 1000
                    sliderImageViewHeight = typedArray.getDimensionPixelSize(R.styleable.BannerSlider_sliderImageHeight, resources.getDimensionPixelSize(R.dimen.default_slider_height))
                    indicatorActiveColor = typedArray.getColor(R.styleable.BannerSlider_indicatorActiveColor, ContextCompat.getColor(context, R.color.default_indicator_color_selected))
                    indicatorInactiveColor = typedArray.getColor(R.styleable.BannerSlider_indicatorInactiveColor, ContextCompat.getColor(context, R.color.default_indicator_color_unselected))

                    if (sliderPlaceHolderImage == null)
                        ContextCompat.getDrawable(context, R.drawable.image_shadow)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    typedArray.recycle()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmOverloads
    fun addSlides(slideList: List<Slide>?, imageDefault: Drawable? = ContextCompat.getDrawable(context, R.drawable.image_shadow),
                  transformer: PageTransformer? = null) {
        if (slideList == null || slideList.isEmpty()) return

        sliderPlaceHolderImage = imageDefault

        if (sliderErrorImage == null) {
            sliderErrorImage = ContextCompat.getDrawable(context, R.drawable.ic_default_image)
        }

        viewPager = LooperWrapViewPager(context)
        viewPager!!.id = generateViewId()
        if (transformer != null) viewPager!!.setPageTransformer(false, transformer) else viewPager!!.setPageTransformer(false, TransformersGroup().getTransformer(context, defaultTransition))
        val transformation = getImageTransformation(sliderTransformation)
        viewPager!!.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        viewPager!!.addOnPageChangeListener(this@Slider)
        viewPager!!.clipChildren = false
        viewPager!!.clipToPadding = false
        viewPager!!.offscreenPageLimit = 3
        viewPager!!.pageMargin = pageMargin
        viewPager!!.setPadding(sliderPaddingLeft, sliderPaddingTop, sliderPaddingRight, sliderPaddingBottom)
        addView(viewPager)
        val adapter = SliderAdapter(context, slideList, sliderPlaceHolderImage!!,
                sliderErrorImage!!, sliderImageViewHeight, transformation) { adapterView, view, i, l -> if (itemClickListener != null) itemClickListener!!.onItemClick(adapterView, view, i, l) }
        viewPager!!.adapter = adapter
        slideCount = slideList.size
        viewPager!!.currentItem = 0
        if (!hideIndicators && slideCount > 1) {
            slideIndicatorsGroup = SlideIndicatorsGroup(context,
                    defaultIndicator,
                    indicatorSize,
                    mustAnimateIndicators,
                    indicatorActiveColor,
                    indicatorInactiveColor
            )
            addView(slideIndicatorsGroup)
            slideIndicatorsGroup!!.setSlides(slideCount)
            slideIndicatorsGroup!!.onSlideChange(0)
        }
        if (slideCount > 1) setupTimer()
    }

    private fun getImageTransformation(sliderTransformation: Int): Transformation<Bitmap> {
        return when (sliderTransformation) {
            1 -> CenterCrop()
            2 -> FitCenter()
            else -> CircleCrop()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        currentPageNumber = position
        if (slideIndicatorsGroup != null && !hideIndicators) {
            when (position) {
                0 -> {
                    slideIndicatorsGroup!!.onSlideChange(slideCount - 1)
                }
                slideCount + 1 -> {
                    slideIndicatorsGroup!!.onSlideChange(0)
                }
                else -> {
                    slideIndicatorsGroup!!.onSlideChange(position - 1)
                }
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        when (state) {
            ViewPager.SCROLL_STATE_DRAGGING -> sliderHandler.removeCallbacksAndMessages(null)
            ViewPager.SCROLL_STATE_IDLE -> setupTimer()
        }
    }

    private fun setupTimer() {
        try {
            if (mustLoopSlides) {
                sliderHandler.postDelayed(object : Runnable {
                    override fun run() {
                        try {
                            if (currentPageNumber < slideCount) currentPageNumber += 1 else currentPageNumber = 1
                            viewPager!!.setCurrentItem(currentPageNumber - 1, true)
                            sliderHandler.removeCallbacksAndMessages(null)
                            sliderHandler.postDelayed(this, slideShowInterval.toLong())
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }, slideShowInterval.toLong())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // setters
    fun setItemClickListener(itemClickListener: OnItemClickListener?) {
        this.itemClickListener = itemClickListener
    }

    fun setHideIndicators(hideIndicators: Boolean) {
        this.hideIndicators = hideIndicators
        try {
            if (hideIndicators) slideIndicatorsGroup!!.visibility = INVISIBLE else slideIndicatorsGroup!!.visibility = VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val TAG = "SLIDER"
    }
}