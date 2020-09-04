package com.kurio.carousel.ui.adapter

import android.os.Parcelable
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

class LoopPagerAdapterWrapper(val realAdapter: PagerAdapter) : PagerAdapter() {
    private var mToDestroy = SparseArray<ToDestroy>()
    private var mBoundaryCaching = false
    fun setBoundaryCaching(flag: Boolean) {
        mBoundaryCaching = flag
    }

    override fun notifyDataSetChanged() {
        mToDestroy = SparseArray()
        super.notifyDataSetChanged()
    }

    fun toRealPosition(position: Int): Int {
        val realCount = realCount
        if (realCount == 0) return 0
        var realPosition = (position - 1) % realCount
        if (realPosition < 0) realPosition += realCount
        return realPosition
    }

    fun toInnerPosition(realPosition: Int): Int {
        return realPosition + 1
    }

    private val realFirstPosition: Int
        get() = 1
    private val realLastPosition: Int
        get() = realFirstPosition + realCount - 1

    override fun getCount(): Int {
        return if (realAdapter.count == 1) {
            1
        } else {
            realAdapter.count + 2
        }
    }

    val realCount: Int
        get() = realAdapter.count

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val realPosition = if (realAdapter is FragmentPagerAdapter || realAdapter is FragmentStatePagerAdapter) position else toRealPosition(position)
        if (mBoundaryCaching) {
            val toDestroy = mToDestroy[position]
            if (toDestroy != null) {
                mToDestroy.remove(position)
                return toDestroy.item
            }
        }
        return realAdapter.instantiateItem(container, realPosition)
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        val realFirst = realFirstPosition
        val realLast = realLastPosition
        val realPosition = if (realAdapter is FragmentPagerAdapter || realAdapter is FragmentStatePagerAdapter) position else toRealPosition(position)
        if (mBoundaryCaching && (position == realFirst || position == realLast)) {
            mToDestroy.put(position, ToDestroy(realPosition, item))
        } else {
            realAdapter.destroyItem(container, realPosition, item)
        }
    }

    /*
     * Delegate rest of methods directly to the inner adapter.
     */
    override fun finishUpdate(container: ViewGroup) {
        realAdapter.finishUpdate(container)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return realAdapter.isViewFromObject(view, `object`)
    }

    override fun restoreState(bundle: Parcelable?, classLoader: ClassLoader?) {
        realAdapter.restoreState(bundle, classLoader)
    }

    override fun saveState(): Parcelable? {
        return realAdapter.saveState()
    }

    override fun startUpdate(container: ViewGroup) {
        realAdapter.startUpdate(container)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        realAdapter.setPrimaryItem(container, position, `object`)
    }
    /*
     * End delegation
     */
    /**
     * Container class for caching the boundary views
     */
    internal class ToDestroy(var position: Int, var item : Any)
}