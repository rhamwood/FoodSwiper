package org.rowanhamwood.hungr.recipelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.rowanhamwood.hungr.*
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.ui.recipelist.RecipeListFragment
import org.rowanhamwood.hungr.utils.atPosition
import javax.inject.Inject
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import org.hamcrest.CoreMatchers.not


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RecipeListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var repository: BaseRecipesRepository


    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }


    @Test
    fun FavouriteRecipeDisplayedInUi()  {

        //Given
        repository.insertRecipeBlocking(recipeModel1)

        //When
        launchFragmentInHiltContainer<RecipeListFragment>{}

        //Then
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()))
        onView(withId(R.id.recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText("Chess Pie")))))
        onView(withId(R.id.recyclerview))
            .check(matches(atPosition(0, hasDescendant(withText("Good Housekeeping")))))


    }

    @Test
    fun FavouriteRecipesOnswipeIsDeleted() {

        //Given
        repository.insertRecipeBlocking(recipeModel1)

        //When
        launchFragmentInHiltContainer<RecipeListFragment>{}

        //Then
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            swipeLeft()))
        onView(withId(R.id.recyclerview))
            .check(matches(atPosition(0, not(isDisplayed()))))


    }
}



