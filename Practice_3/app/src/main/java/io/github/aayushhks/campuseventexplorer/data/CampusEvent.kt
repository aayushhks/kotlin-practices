package io.github.aayushhks.campuseventexplorer.data

/** One event in the campus collection. [id] is unique and never changes. */
data class CampusEvent(
    val id: Long,
    val title: String,
    val category: String,
    val location: String,
    val time: String,
    val description: String,
)

/**
 * Returns the events whose title or category contains [query], ignoring case.
 * A blank query returns every event. The result is always computed from the
 * query, so the screen never keeps its own copy of a filtered list.
 */
fun filterEvents(events: List<CampusEvent>, query: String): List<CampusEvent> {
    val text = query.trim()
    if (text.isEmpty()) return events
    return events.filter { event ->
        event.title.contains(text, ignoreCase = true) ||
            event.category.contains(text, ignoreCase = true)
    }
}
