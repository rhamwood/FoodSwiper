package org.rowanhamwood.hungr.swipe

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yuyakaido.android.cardstackview.CardStackView
import dagger.Binds
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.rowanhamwood.hungr.*
import org.rowanhamwood.hungr.data.source.AndroidFakeTestRepository
import org.rowanhamwood.hungr.repository.BaseRecipesRepository
import org.rowanhamwood.hungr.ui.swipe.SwipeFragment
import org.rowanhamwood.hungr.utils.atPosition
import org.rowanhamwood.hungr.viewmodel.RecipeViewModel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SwipeFragmentTest {


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
    fun SwipeCardDisplayedinUi(){

        repository.getRecipesBlocking("pie", null, null, false,false )

        //When
        launchFragmentInHiltContainer<SwipeFragment> {  }


        //Then
        onView(withId(R.id.card_stack_view)).check(matches(atPosition(0, withText("Chess Pie"))))
//        onView(withId(R.id.item_image))
//            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))



    }

    @Test
    fun ErrorTextAndImageShowResultFailure() {

//        repository.getRecipesBlocking(null, null, null, false,false )

        //
        launchFragmentInHiltContainer<SwipeFragment>{}


        //Then
        onView(withId(R.id.swipeErrorImage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.swipeErrorText))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }





}