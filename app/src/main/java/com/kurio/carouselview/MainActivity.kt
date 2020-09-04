package com.kurio.carouselview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kurio.carousel.model.Slide
import com.kurio.carousel.ui.Slider
import java.util.*

class MainActivity : AppCompatActivity() {
    private val carouselSlider: Slider by lazy { findViewById(R.id.carouselSlider) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addSlider()
    }

    private fun addSlider() {
        val slideList: MutableList<Slide> = ArrayList()
        slideList.add(Slide("https://images.unsplash.com/photo-1486728297118-82a07bc48a28?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1401&q=80", resources.getDimensionPixelSize(R.dimen.slider_image_corner)))
        slideList.add(Slide("https://images.unsplash.com/photo-1469594292607-7bd90f8d3ba4?ixlib=rb-1.2.1&auto=format&fit=crop&w=1350&q=80", resources.getDimensionPixelSize(R.dimen.slider_image_corner)))
        slideList.add(Slide("https://images.unsplash.com/photo-1505804080740-b5b47214447d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1489&q=80", resources.getDimensionPixelSize(R.dimen.slider_image_corner)))
        slideList.add(Slide("https://images.unsplash.com/photo-1569166854693-f73a09017586?ixlib=rb-1.2.1&auto=format&fit=crop&w=1951&q=80", resources.getDimensionPixelSize(R.dimen.slider_image_corner)))
        carouselSlider.setItemClickListener { _, _, i, _ -> Toast.makeText(applicationContext, i.toString() + "", Toast.LENGTH_SHORT).show() }

        //add slides to slider
        carouselSlider.addSlides(slideList)

        //Hide Slider
//        carouselSlider.setHideIndicators(true)
    }
}