package org.rowanhamwood.hungr

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.utils.DataBindingIdlingResource
import org.rowanhamwood.hungr.utils.EspressoIdlingResource
import org.rowanhamwood.hungr.utils.monitorActivity
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AppNavigationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


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
    fun bottomNavigationGoBetweenAllScreens() {

        //Start up Swipe Screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //click on bottom nav search icon
        onView(withId(R.id.navigation_search)).perform(click())

        // Check that search screen was opened
        onView(withId(R.id.search_layout)).check(ViewAssertions.matches(isDisplayed()))

        //click on bottom nav recipelist icon
        onView(withId(R.id.navigation_recipe_list)).perform(click())

        //Check that recipelist screen was opened
        onView(withId(R.id.recipelist_layout)).check(ViewAssertions.matches(isDisplayed()))

        //click on bottom nav swipe icon
        onView(withId(R.id.navigation_swipe)).perform(click())

        //Check that swipe screen was opened
        onView(withId(R.id.swipe_layout)).check(ViewAssertions.matches(isDisplayed()))

        //close activity scenario
        activityScenario.close()

    }


}