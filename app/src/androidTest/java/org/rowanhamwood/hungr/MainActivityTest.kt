package org.rowanhamwood.hungr

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.anything
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.utils.DataBindingIdlingResource
import org.rowanhamwood.hungr.utils.EspressoIdlingResource
import org.rowanhamwood.hungr.utils.atPosition
import org.rowanhamwood.hungr.utils.monitorActivity
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var repository: BaseRecipesRepository

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }


    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun completeSearchAndCheckthatSwipeViewisFilled(){

        //Start up Swipe Screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //click on bottom nav search icon
        onView(withId(R.id.navigation_search)).perform(click())

        //Complete Search
        onView(withId(R.id.searchView)).perform(click())
        onView(withId(R.id.searchView)).perform(typeText("pie"))
        closeSoftKeyboard()
        onView(withId(R.id.submitButton)).perform(click())

        //check that swipe view is opened and that cardview shows recipes
        onView(withId(R.id.swipe_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.card_stack_view)).check(matches(isDisplayed()))
            .check(matches(atPosition(0, hasDescendant(withText("Chess Pie")))))
        onView(withId(R.id.card_stack_view))
            .check(matches(atPosition(0, hasDescendant(withText("Good Housekeeping")))))

        //close the activity scenario
        activityScenario.close()
    }




}