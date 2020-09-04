package com.kurio.carouselview

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)


    @Test
    fun swipeLeft() {
        ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(withId(R.id.carouselSlider))
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.swipeLeft()).perform(ViewActions.swipeLeft())
    }

    @Test
    fun swipeRight() {
        ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(withId(R.id.carouselSlider))
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.swipeRight()).perform(ViewActions.swipeRight())
    }

    @Test
    fun swipeRightAndClick() {
        ActivityScenario.launch(MainActivity::class.java)

        Espresso.onView(withId(R.id.carouselSlider))
                .check(matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click()).perform(ViewActions.swipeRight())
    }

}