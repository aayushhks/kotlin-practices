package io.github.aayushhks.mobilitylens

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aayushhks.mobilitylens.ui.theme.MobilityLensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MobilityLensTheme {
                MobilityLensScreen()
            }
        }
    }
}

/**
 * The single screen of the application.
 *
 * Note on state: every value below is held with [remember] rather than
 * rememberSaveable. That is deliberate for this assignment, so the rotation
 * observation shows what is lost when the activity is recreated.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobilityLensScreen(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var designNameInput by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var isInputInvalid by remember { mutableStateOf(false) }

    val dimension = mobilityDimensions[selectedIndex]
    val dimensionTitle = stringResource(dimension.titleRes)
    val blankInputMessage = stringResource(R.string.feedback_blank_input)
    val matchMessage = stringResource(R.string.feedback_match, designNameInput.trim(), dimensionTitle)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.app_introduction),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            DimensionCard(
                position = stringResource(
                    R.string.dimension_position,
                    selectedIndex + 1,
                    mobilityDimensions.size,
                ),
                progress = (selectedIndex + 1).toFloat() / mobilityDimensions.size,
                title = dimensionTitle,
                constraint = stringResource(dimension.constraintRes),
                implication = stringResource(dimension.implicationRes),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = {
                        selectedIndex--
                        feedbackMessage = ""
                        isInputInvalid = false
                    },
                    enabled = selectedIndex > 0,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.action_previous))
                }
                Button(
                    onClick = {
                        selectedIndex++
                        feedbackMessage = ""
                        isInputInvalid = false
                    },
                    enabled = selectedIndex < mobilityDimensions.lastIndex,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(stringResource(R.string.action_next))
                }
            }

            HorizontalDivider()

            Text(
                text = stringResource(R.string.input_section_heading),
                style = MaterialTheme.typography.titleMedium,
            )

            OutlinedTextField(
                value = designNameInput,
                onValueChange = {
                    designNameInput = it
                    isInputInvalid = false
                    feedbackMessage = ""
                },
                label = { Text(stringResource(R.string.input_label)) },
                placeholder = { Text(stringResource(R.string.input_placeholder)) },
                singleLine = true,
                isError = isInputInvalid,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    if (designNameInput.isBlank()) {
                        isInputInvalid = true
                        feedbackMessage = blankInputMessage
                    } else {
                        isInputInvalid = false
                        feedbackMessage = matchMessage
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.action_check))
            }

            if (feedbackMessage.isNotEmpty()) {
                Text(
                    text = feedbackMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (isInputInvalid) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                )
            }
        }
    }
}

/** Shows the currently selected dimension, its constraint and a developer implication. */
@Composable
private fun DimensionCard(
    position: String,
    progress: Float,
    title: String,
    constraint: String,
    implication: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = position,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
            )
            LabelledParagraph(
                label = stringResource(R.string.label_constraint),
                body = constraint,
            )
            LabelledParagraph(
                label = stringResource(R.string.label_implication),
                body = implication,
            )
        }
    }
}

@Composable
private fun LabelledParagraph(label: String, body: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true, name = "Mobility Lens - light")
@Composable
private fun MobilityLensScreenPreview() {
    MobilityLensTheme {
        MobilityLensScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Mobility Lens - dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun MobilityLensScreenDarkPreview() {
    MobilityLensTheme {
        MobilityLensScreen()
    }
}