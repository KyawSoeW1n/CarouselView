/*
 * Copyright (C) 2013 Leszek Mzyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kurio.carousel.ui.customUI

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.kurio.carousel.ui.adapter.LoopPagerAdapterWrapper

class LooperWrapViewPager : ViewPager {
    var mOuterPageChangeListener: OnPageChangeListener? = null
    private var mAdapter: LoopPagerAdapterWrapper? = null
    private var mBoundaryCaching = DEFAULT_BOUNDARY_CASHING

    /**
     * If set to true, the boundary views (i.e. first and last) will never be destroyed
     * This may help to prevent "blinking" of some views
     *
     * @param flag
     */
    fun setBoundaryCaching(flag: Boolean) {
        mBoundaryCaching = flag
        if (mAdapter != null) {
            mAdapter!!.setBoundaryCaching(flag)
        }
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        mAdapter = LoopPagerAdapterWrapper(adapter!!)
        mAdapter!!.setBoundaryCaching(mBoundaryCaching)
        super.setAdapter(mAdapter)
        setCurrentItem(0, false)
    }

    override fun getAdapter(): PagerAdapter? {
        return if (mAdapter != null) mAdapter!!.realAdapter else mAdapter
    }

    override fun getCurrentItem(): Int {
        return if (mAdapter != null) mAdapter!!.toRealPosition(super.getCurrentItem()) else 0
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        val realItem = mAdapter!!.toInnerPosition(item)
        if (mAdapter!!.count > 1) super.setCurrentItem(realItem, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        if (currentItem != item && mAdapter!!.count > 1) {
            setCurrentItem(item, true)
        }
    }

    override fun setOnPageChangeListener(listener: OnPageChangeListener) {
        mOuterPageChangeListener = listener
    }

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    private fun init() {
        super.addOnPageChangeListener(onPageChangeListener)
    }

    private val onPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        private var mPreviousOffset = -1f
        private var mPreviousPosition = -1f
        override fun onPageSelected(position: Int) {
            val realPosition = mAdapter!!.toRealPosition(position)
            if (mPreviousPosition != realPosition.toFloat()) {
                mPreviousPosition = realPosition.toFloat()
                if (mOuterPageChangeListener != null) {
                    mOuterPageChangeListener!!.onPageSelected(realPosition)
                }
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float,
                                    positionOffsetPixels: Int) {
            var realPosition = position
            if (mAdapter != null) {
                realPosition = mAdapter!!.toRealPosition(position)
                if (positionOffset == 0f && mPreviousOffset == 0f && (position == 0 || position == mAdapter!!.count - 1)) {
                    setCurrentItem(realPosition, false)
                }
            }
            mPreviousOffset = positionOffset
            if (mOuterPageChangeListener != null) {
                if (realPosition != mAdapter!!.realCount - 1) {
                    mOuterPageChangeListener!!.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
                } else {
                    if (positionOffset > .5) {
                        mOuterPageChangeListener!!.onPageScrolled(0, 0f, 0)
                    } else {
                        mOuterPageChangeListener!!.onPageScrolled(realPosition, 0f, 0)
                    }
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (mAdapter != null) {
                val position = super@LooperWrapViewPager.getCurrentItem()
                val realPosition = mAdapter!!.toRealPosition(position)
                if (state == SCROLL_STATE_IDLE
                        && (position == 0 || position == mAdapter!!.count - 1)) {
                    setCurrentItem(realPosition, false)
                }
            }
            if (mOuterPageChangeListener != null) {
                mOuterPageChangeListener!!.onPageScrollStateChanged(state)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasure: Int) {
        var heightMeasureSpec = heightMeasure
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) height = h
        }
        if (height != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    companion object {
        private const val DEFAULT_BOUNDARY_CASHING = false

        /**
         * helper function which may be used when implementing FragmentPagerAdapter
         *
         * @param position
         * @param count
         * @return (position-1)%count
         */
        fun toRealPosition(pos: Int, count: Int): Int {
            var position = pos
            position -= 1
            if (position < 0) {
                position += count
            } else {
                position %= count
            }
            return position
        }
    }
}