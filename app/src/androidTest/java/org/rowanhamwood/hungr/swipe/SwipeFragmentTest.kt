package org.rowanhamwood.hungr.swipe


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.rowanhamwood.hungr.*
import org.rowanhamwood.hungr.ui.swipe.SwipeFragment


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SwipeFragmentTest {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }


    @Test
    fun ErrorTextAndImageShowResultFailure() {

        //When
        launchFragmentInHiltContainer<SwipeFragment>{}

        //Then
        onView(withId(R.id.swipeErrorImage))
            .check(matches(isDisplayed()))
        onView(withId(R.id.swipeErrorText))
            .check(matches(isDisplayed()))

    }





}