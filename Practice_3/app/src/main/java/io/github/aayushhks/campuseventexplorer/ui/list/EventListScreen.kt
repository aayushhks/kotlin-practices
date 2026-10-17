package io.github.aayushhks.campuseventexplorer.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aayushhks.campuseventexplorer.R
import io.github.aayushhks.campuseventexplorer.data.CampusEvent
import io.github.aayushhks.campuseventexplorer.data.CampusEvents
import io.github.aayushhks.campuseventexplorer.data.filterEvents
import io.github.aayushhks.campuseventexplorer.ui.common.CategoryLabel
import io.github.aayushhks.campuseventexplorer.ui.common.IconLine
import io.github.aayushhks.campuseventexplorer.ui.theme.CampusEventTheme

/** Tags the UI tests use to find parts of the list screen. */
object EventListTestTags {
    const val SEARCH_FIELD = "search_field"
    const val LIST = "event_list"
    fun row(eventId: Long) = "event_row_$eventId"
}

/**
 * Stateful entry for the list destination. It owns the search text and works
 * out the visible events from it on every change, so there is no second list
 * that could drift out of sync with the query.
 */
@Composable
fun EventListRoute(
    events: List<CampusEvent>,
    onEventSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val visibleEvents = remember(events, query) { filterEvents(events, query) }

    EventListScreen(
        events = visibleEvents,
        query = query,
        onQueryChange = { query = it },
        onEventSelected = onEventSelected,
        modifier = modifier,
    )
}

/** Stateless list screen. It draws what it is given and reports clicks upward. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    events: List<CampusEvent>,
    query: String,
    onQueryChange: (String) -> Unit,
    onEventSelected: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.list_title)) }) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding(),
        ) {
            SearchField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { focusManager.clearFocus() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            if (events.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_results, query.trim()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(EventListTestTags.LIST),
                    contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    // The event ID is the stable key, so Compose tracks each row by
                    // identity when the filter adds or removes rows.
                    items(items = events, key = { event -> event.id }) { event ->
                        EventRow(
                            event = event,
                            onClick = {
                                focusManager.clearFocus()
                                onEventSelected(event.id)
                            },
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.testTag(EventListTestTags.SEARCH_FIELD),
        label = { Text(stringResource(R.string.search_label)) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = stringResource(R.string.clear_search))
                }
            }
        } else {
            null
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
    )
}

@Composable
private fun EventRow(
    event: CampusEvent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag(EventListTestTags.row(event.id)),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            CategoryLabel(category = event.category)
            Text(text = event.title, style = MaterialTheme.typography.titleMedium)
            IconLine(icon = Icons.Filled.DateRange, text = event.time)
            IconLine(icon = Icons.Filled.LocationOn, text = event.location)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventListScreenPreview() {
    CampusEventTheme {
        EventListScreen(
            events = CampusEvents.all.take(4),
            query = "",
            onQueryChange = {},
            onEventSelected = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EventListNoResultsPreview() {
    CampusEventTheme {
        EventListScreen(
            events = emptyList(),
            query = "quidditch",
            onQueryChange = {},
            onEventSelected = {},
        )
    }
}
