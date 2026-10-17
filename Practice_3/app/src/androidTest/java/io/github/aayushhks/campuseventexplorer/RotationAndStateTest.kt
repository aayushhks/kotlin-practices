package io.github.aayushhks.campuseventexplorer

import android.content.res.Configuration
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import io.github.aayushhks.campuseventexplorer.data.CampusEvents
import io.github.aayushhks.campuseventexplorer.ui.detail.EventDetailTestTags
import io.github.aayushhks.campuseventexplorer.ui.list.EventListTestTags
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Runs the rotation and state sequence from the assignment, step by step. */
@RunWith(AndroidJUnit4::class)
class RotationAndStateTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    @After
    fun restoreOrientation() {
        device.setOrientationNatural()
        device.unfreezeRotation()
    }

    @Test
    fun detailSurvivesRotationAndSearchSurvivesBack() {
        val query = "career"
        val firstPick = CampusEvents.all.first { it.id == 102L }
        val secondPick = CampusEvents.all.first { it.id == 105L }
        val filteredOut = 101L

        // 1. Launch the application.
        composeTestRule.onNodeWithTag(EventListTestTags.row(filteredOut)).assertIsDisplayed()

        // 2. Enter search text that reduces the list.
        composeTestRule.onNodeWithTag(EventListTestTags.SEARCH_FIELD).performTextInput(query)
        Espresso.closeSoftKeyboard()
        composeTestRule.onNodeWithTag(EventListTestTags.row(filteredOut)).assertDoesNotExist()
        composeTestRule.onNodeWithTag(EventListTestTags.row(firstPick.id)).assertIsDisplayed()

        // 3. Select an event.
        composeTestRule.onNodeWithTag(EventListTestTags.row(firstPick.id)).performClick()
        composeTestRule.onNodeWithText(firstPick.title).assertIsDisplayed()

        // 4. Rotate the emulator.
        rotateToLandscape()

        // 5. Verify that the detail destination remains visible.
        composeTestRule.onNodeWithTag(EventDetailTestTags.DETAILS).assertIsDisplayed()
        composeTestRule.onNodeWithText(firstPick.title).assertIsDisplayed()

        // 6. Press Back.
        Espresso.pressBack()

        // 7. Verify that the search text remains (and the list is still filtered).
        composeTestRule.onNodeWithTag(EventListTestTags.SEARCH_FIELD).assertTextContains(query)
        composeTestRule.onNodeWithTag(EventListTestTags.row(filteredOut)).assertDoesNotExist()

        // 8. Select another event.
        composeTestRule.onNodeWithTag(EventListTestTags.LIST).performScrollToKey(secondPick.id)
        composeTestRule.onNodeWithTag(EventListTestTags.row(secondPick.id)).performClick()
        composeTestRule.onNodeWithText(secondPick.title).assertIsDisplayed()

        // 9. Press system Back.
        device.pressBack()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag(EventListTestTags.LIST).assertIsDisplayed()
        composeTestRule.onNodeWithTag(EventDetailTestTags.DETAILS).assertDoesNotExist()
        composeTestRule.onNodeWithTag(EventListTestTags.SEARCH_FIELD).assertTextContains(query)
    }

    /** Rotates the emulator and waits until the Activity has been recreated in landscape. */
    private fun rotateToLandscape() {
        val before = currentActivity()
        device.setOrientationLandscape()
        composeTestRule.waitUntil(timeoutMillis = 5_000) {
            val now = currentActivity()
            now != null &&
                now !== before &&
                now.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        }
        composeTestRule.waitForIdle()
    }

    /** The resumed Activity, or null while it is between being destroyed and recreated. */
    private fun currentActivity(): MainActivity? = runCatching {
        var activity: MainActivity? = null
        composeTestRule.activityRule.scenario.onActivity { activity = it }
        activity
    }.getOrNull()
}
