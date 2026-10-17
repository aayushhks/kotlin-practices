package io.github.aayushhks.campuseventexplorer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.aayushhks.campuseventexplorer.data.CampusEvent
import io.github.aayushhks.campuseventexplorer.data.CampusEvents
import io.github.aayushhks.campuseventexplorer.navigation.EventDetail
import io.github.aayushhks.campuseventexplorer.navigation.EventList
import io.github.aayushhks.campuseventexplorer.share.shareEvent
import io.github.aayushhks.campuseventexplorer.ui.detail.EventDetailScreen
import io.github.aayushhks.campuseventexplorer.ui.list.EventListRoute

/**
 * Owns the NavController and wires navigation. The screens below only receive
 * values and callbacks, never the NavController itself.
 */
@Composable
fun CampusEventApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    events: List<CampusEvent> = CampusEvents.all,
) {
    NavHost(
        navController = navController,
        startDestination = EventList,
        modifier = modifier,
    ) {
        composable<EventList> {
            EventListRoute(
                events = events,
                onEventSelected = { eventId ->
                    // Pass identity, not the object: only the ID goes into the route.
                    navController.navigate(EventDetail(eventId = eventId)) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<EventDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<EventDetail>()
            val event = events.firstOrNull { it.id == route.eventId }
            val context = LocalContext.current

            EventDetailScreen(
                event = event,
                // Pops back to the list. Safe to call twice, so a double tap on Up
                // cannot pop the list itself and leave an empty screen.
                onBack = { navController.popBackStack(route = EventList, inclusive = false) },
                onShare = { event?.let { context.shareEvent(it) } },
            )
        }
    }
}
