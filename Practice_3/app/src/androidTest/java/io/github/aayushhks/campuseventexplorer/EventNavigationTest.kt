package io.github.aayushhks.campuseventexplorer

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.aayushhks.campuseventexplorer.data.CampusEvents
import io.github.aayushhks.campuseventexplorer.data.shareText
import io.github.aayushhks.campuseventexplorer.navigation.EventDetail
import io.github.aayushhks.campuseventexplorer.ui.detail.EventDetailTestTags
import io.github.aayushhks.campuseventexplorer.ui.list.EventListTestTags
import io.github.aayushhks.campuseventexplorer.ui.theme.CampusEventTheme
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Up button, invalid IDs and the share intent, tested against the real NavHost. */
@RunWith(AndroidJUnit4::class)
class EventNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        Intents.init()
        composeTestRule.setContent {
            navController = rememberNavController()
            CampusEventTheme {
                CampusEventApp(navController = navController)
            }
        }
        composeTestRule.waitForIdle()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun upButtonReturnsToList() {
        composeTestRule.onNodeWithTag(EventListTestTags.row(101L)).performClick()
        composeTestRule.onNodeWithTag(EventDetailTestTags.DETAILS).assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.navigate_up))
            .performClick()

        composeTestRule.onNodeWithTag(EventListTestTags.LIST).assertIsDisplayed()
        composeTestRule.onNodeWithTag(EventDetailTestTags.DETAILS).assertDoesNotExist()
    }

    @Test
    fun unknownIdShowsUnavailableAndReturnsToList() {
        composeTestRule.runOnUiThread { navController.navigate(EventDetail(eventId = 9_999L)) }

        composeTestRule.onNodeWithTag(EventDetailTestTags.UNAVAILABLE).assertIsDisplayed()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.return_to_events))
            .performClick()

        composeTestRule.onNodeWithTag(EventListTestTags.LIST).assertIsDisplayed()
    }

    @Test
    fun shareButtonSendsImplicitChooserIntent() {
        val meetup = CampusEvents.all.first { it.id == 101L }
        // Stub the chooser so the test does not leave a share sheet open.
        intending(hasAction(Intent.ACTION_CHOOSER))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        composeTestRule.onNodeWithTag(EventListTestTags.row(meetup.id)).performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.share_event))
            .performScrollTo()
            .performClick()

        intended(
            allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(
                    equalTo(Intent.EXTRA_INTENT),
                    allOf(
                        hasAction(Intent.ACTION_SEND),
                        hasExtra(Intent.EXTRA_TEXT, meetup.shareText()),
                    ),
                ),
            ),
        )
    }
}
