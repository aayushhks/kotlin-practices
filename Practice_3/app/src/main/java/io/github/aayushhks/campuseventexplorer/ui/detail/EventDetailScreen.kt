package io.github.aayushhks.campuseventexplorer.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aayushhks.campuseventexplorer.R
import io.github.aayushhks.campuseventexplorer.data.CampusEvent
import io.github.aayushhks.campuseventexplorer.data.CampusEvents
import io.github.aayushhks.campuseventexplorer.ui.theme.CampusEventTheme

/** Tags the UI tests use to find parts of the detail screen. */
object EventDetailTestTags {
    const val DETAILS = "event_detail"
    const val UNAVAILABLE = "event_unavailable"
}

/**
 * Detail screen. [event] is null when the route's ID did not match any event,
 * and in that case the screen shows a recovery message instead of crashing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    event: CampusEvent?,
    onBack: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        if (event == null) {
            EventUnavailable(onReturn = onBack, modifier = contentModifier)
        } else {
            EventDetails(event = event, onShare = onShare, modifier = contentModifier)
        }
    }
}

@Composable
private fun EventDetails(
    event: CampusEvent,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag(EventDetailTestTags.DETAILS),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = event.title, style = MaterialTheme.typography.headlineSmall)

        DetailField(
            icon = Icons.Filled.Star,
            label = stringResource(R.string.label_category),
            value = event.category,
        )
        DetailField(
            icon = Icons.Filled.DateRange,
            label = stringResource(R.string.label_time),
            value = event.time,
        )
        DetailField(
            icon = Icons.Filled.LocationOn,
            label = stringResource(R.string.label_location),
            value = event.location,
        )

        HorizontalDivider()

        Text(
            text = stringResource(R.string.label_about),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(text = event.description, style = MaterialTheme.typography.bodyLarge)

        Button(onClick = onShare, modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text(stringResource(R.string.share_event))
        }
    }
}

@Composable
private fun DetailField(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun EventUnavailable(
    onReturn: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(24.dp)
            .testTag(EventDetailTestTags.UNAVAILABLE),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.event_unavailable),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.event_unavailable_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onReturn) {
            Text(stringResource(R.string.return_to_events))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventDetailScreenPreview() {
    CampusEventTheme {
        EventDetailScreen(event = CampusEvents.all.first(), onBack = {}, onShare = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun EventUnavailablePreview() {
    CampusEventTheme {
        EventDetailScreen(event = null, onBack = {}, onShare = {})
    }
}
