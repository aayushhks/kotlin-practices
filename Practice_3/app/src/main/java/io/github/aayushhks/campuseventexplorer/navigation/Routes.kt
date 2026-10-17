package io.github.aayushhks.campuseventexplorer.navigation

import kotlinx.serialization.Serializable

/** Start destination: the searchable list of events. */
@Serializable
data object EventList

/** Detail destination. Only the event's ID travels through navigation. */
@Serializable
data class EventDetail(
    val eventId: Long,
)
