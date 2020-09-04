package com.kurio.carousel.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kurio.carousel.R
import com.kurio.carousel.model.Slide
import com.kurio.carousel.ui.Slider
import java.util.*

class SliderAdapter(context: Context, items: List<Slide>, sliderPlaceHolderImage: Drawable,
                    sliderErrorImage: Drawable, imageViewHeight: Int,
                    transformation: Transformation<Bitmap>, itemClickListener: OnItemClickListener?) : PagerAdapter() {
    private val layoutInflater: LayoutInflater
    private val itemClickListener: OnItemClickListener?
    private var items: List<Slide> = ArrayList()
    private val sliderPlaceHolderImage: Drawable
    private val sliderErrorImage: Drawable
    private val imageViewHeight: Int
    private var transformation: Transformation<Bitmap>
    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        // The object returned by instantiateItem() is a key/identifier. This method checks whether
        // the View passed to it (representing the page) is associated with that key or not.
        // It is required by a PagerAdapter to function properly.
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = layoutInflater.inflate(R.layout.row_slider, container, false)
        val sliderImage = view.findViewById<ImageView>(R.id.sliderImage)
        sliderImage.layoutParams.height = imageViewHeight
        loadImage(sliderImage, items[position].image, sliderPlaceHolderImage, sliderErrorImage)
        val parent = view.findViewById<View>(R.id.parent)
        parent.setOnClickListener { itemClickListener?.onItemClick(null, null, position, 0) }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        // Removes the page from the container for the given position. We simply removed object using removeView()
        // but could’ve also used removeViewAt() by passing it the position.
        val obj: Any? = item
        try {
            // Remove the view from the container
            container.removeView(obj as View?)
            // Invalidate the object
        } catch (e: Exception) {
            Log.w(Slider.TAG, "destroyItem: failed to destroy item and clear it's used resources", e)
        }
    }

    /**
     * Display the gallery image into the image view provided.
     */
    private fun loadImage(imageView: ImageView, url: String, imagePlaceHolder: Drawable, imageError: Drawable) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(imageView.context) // Bind it with the context of the actual view used
                    .load(url) // Load the image
                    .apply(RequestOptions.bitmapTransform(transformation))
                    .placeholder(imagePlaceHolder)
                    .error(imageError)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
        }
    }

    init {
        this.items = items
        this.itemClickListener = itemClickListener
        layoutInflater = LayoutInflater.from(context)
        this.sliderPlaceHolderImage = sliderPlaceHolderImage
        this.sliderErrorImage = sliderErrorImage
        this.imageViewHeight = imageViewHeight
        this.transformation = transformation
    }
}