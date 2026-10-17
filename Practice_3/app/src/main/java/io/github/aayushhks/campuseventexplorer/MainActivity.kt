package io.github.aayushhks.campuseventexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.aayushhks.campuseventexplorer.data.CampusEvents
import io.github.aayushhks.campuseventexplorer.ui.list.EventListRoute
import io.github.aayushhks.campuseventexplorer.ui.theme.CampusEventTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusEventTheme {
                EventListRoute(events = CampusEvents.all, onEventSelected = {})
            }
        }
    }
}
