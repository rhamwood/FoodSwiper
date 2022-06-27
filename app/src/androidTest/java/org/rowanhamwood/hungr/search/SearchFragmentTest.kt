package org.rowanhamwood.hungr.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.rowanhamwood.hungr.R
import org.rowanhamwood.hungr.launchFragmentInHiltContainer
import org.rowanhamwood.hungr.ui.search.SearchFragment



@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun SelectCuisineAndDietType() {

        //When
        launchFragmentInHiltContainer<SearchFragment> {}

        //Then
        onView(withId(R.id.cuisineTextView)).perform(click())
        onView(withText("Asian")).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
        onView(withId(R.id.cuisineTextView)).check(matches(withText("Asian")))
        onView(withId(R.id.healthTextView)).perform(click())
        onView(withText("Alcohol-Free")).inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
        onView(withId(R.id.healthTextView)).check(matches(withText("Alcohol-Free")))


    }

}