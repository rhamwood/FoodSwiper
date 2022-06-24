package org.rowanhamwood.hungr.swipe

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.rowanhamwood.hungr.R
import org.rowanhamwood.hungr.data.source.AndroidFakeTestRepository
import org.rowanhamwood.hungr.launchFragmentInHiltContainer
import org.rowanhamwood.hungr.recipeModel1
import org.rowanhamwood.hungr.recipeModel2
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.ui.swipe.SwipeFragment
import javax.inject.Inject


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SwipeFragmentTest {


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: BaseRecipesRepository

    @Before
    fun init() {
        // Populate @Inject fields in test class
        hiltRule.inject()
    }

    @Test
    fun SwipeCardDisplayedinUi() {
//        repository.addRecipes(recipeModel1, recipeModel2)

        //When
        launchFragmentInHiltContainer<SwipeFragment>{}


        //Then
        onView(withId(R.id.item_name))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))



    }



}