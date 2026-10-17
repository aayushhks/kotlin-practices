package io.github.aayushhks.campuseventexplorer.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CampusEventsTest {

    private val events = CampusEvents.all

    @Test
    fun collectionHasAtLeastEightEventsWithUniqueIds() {
        assertTrue(events.size >= 8)
        assertEquals(events.size, events.map { it.id }.toSet().size)
    }

    @Test
    fun blankQueryReturnsEveryEvent() {
        assertEquals(events, filterEvents(events, ""))
        assertEquals(events, filterEvents(events, "   "))
    }

    @Test
    fun queryMatchesTitleIgnoringCase() {
        assertEquals(listOf(103L), filterEvents(events, "JAZZ").map { it.id })
    }

    @Test
    fun queryMatchesCategory() {
        assertEquals(listOf(102L, 105L), filterEvents(events, "career").map { it.id })
    }

    @Test
    fun queryWithNoMatchReturnsEmptyList() {
        assertTrue(filterEvents(events, "quidditch").isEmpty())
    }

    @Test
    fun shareTextHasTitleTimeAndLocation() {
        val meetup = events.first { it.id == 101L }
        assertEquals(
            "Mobile Development Meetup — Tue, Oct 20, 6:30 PM — CAS 214",
            meetup.shareText(),
        )
    }
}
