package io.github.aayushhks.focusplanbuilder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.aayushhks.focusplanbuilder.ui.theme.FocusPlanBuilderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            FocusPlanBuilderTheme {
                FocusPlanRoute()
            }
        }
    }
}

/**
 * Owns the state for the screen, validates the input, and builds the plan.
 * It passes plain values and callbacks down to [FocusPlanScreen].
 */
@Composable
fun FocusPlanRoute(modifier: Modifier = Modifier) {
    var subject by rememberSaveable { mutableStateOf("") }
    var minutesText by rememberSaveable { mutableStateOf("") }
    var plan by remember { mutableStateOf<FocusPlan?>(null) }

    val minutes: Int? = minutesText.toIntOrNull()

    val canCreatePlan =
        subject.isNotBlank() &&
                minutes != null &&
                minutes in MIN_STUDY_MINUTES..MAX_STUDY_MINUTES

    FocusPlanScreen(
        subject = subject,
        minutesText = minutesText,
        plan = plan,
        onSubjectChange = {
            subject = it
            plan = null
        },
        onMinutesChange = {
            minutesText = it
            plan = null
        },
        canCreatePlan = canCreatePlan,
        onCreatePlan = {
            if (minutes != null) {
                plan = FocusPlan(
                    subject = subject.trim(),
                    minutes = minutes,
                    category = durationCategory(minutes),
                    breakMinutes = recommendedBreak(minutes),
                )
            }
        },
        modifier = modifier,
    )
}

/**
 * Draws the screen. It holds no state of its own: every value arrives as a
 * parameter and every user action is reported through a callback.
 */
@Composable
fun FocusPlanScreen(
    subject: String,
    minutesText: String,
    plan: FocusPlan?,
    onSubjectChange: (String) -> Unit,
    onMinutesChange: (String) -> Unit,
    canCreatePlan: Boolean,
    onCreatePlan: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val minutesEntered = minutesText.toIntOrNull()
    val showMinutesError = minutesText.isNotEmpty() &&
            (minutesEntered == null || minutesEntered !in MIN_STUDY_MINUTES..MAX_STUDY_MINUTES)

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
    ) {
        Text(
            text = stringResource(R.string.screen_title),
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.screen_instructions),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = subject,
            onValueChange = onSubjectChange,
            label = { Text(stringResource(R.string.label_subject)) },
            placeholder = { Text(stringResource(R.string.placeholder_subject)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = minutesText,
            onValueChange = onMinutesChange,
            label = { Text(stringResource(R.string.label_minutes)) },
            placeholder = { Text(stringResource(R.string.placeholder_minutes)) },
            singleLine = true,
            isError = showMinutesError,
            supportingText = {
                Text(
                    text = if (showMinutesError) {
                        stringResource(R.string.error_minutes_range)
                    } else {
                        stringResource(R.string.hint_minutes_range)
                    },
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCreatePlan,
            enabled = canCreatePlan,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.action_create_plan))
        }

        if (plan != null) {
            Spacer(modifier = Modifier.height(28.dp))
            PlanResultCard(plan = plan)
        }
    }
}

/** Shows a finished plan. Only drawn once a plan exists. */
@Composable
private fun PlanResultCard(plan: FocusPlan, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = plan.subject,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(14.dp))

            PlanDetailRow(
                label = stringResource(R.string.card_label_duration),
                value = stringResource(R.string.card_value_minutes, plan.minutes),
            )
            PlanDetailRow(
                label = stringResource(R.string.card_label_category),
                value = plan.category,
            )
            PlanDetailRow(
                label = stringResource(R.string.card_label_break),
                value = stringResource(R.string.card_value_minutes, plan.breakMinutes),
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(
                    R.string.card_summary,
                    plan.subject,
                    plan.minutes,
                    plan.breakMinutes,
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

/** One label and value pair inside the result card. */
@Composable
private fun PlanDetailRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Preview(showBackground = true, name = "Empty form")
@Composable
private fun FocusPlanScreenEmptyPreview() {
    FocusPlanBuilderTheme {
        Surface {
            FocusPlanScreen(
                subject = "",
                minutesText = "",
                plan = null,
                onSubjectChange = {},
                onMinutesChange = {},
                canCreatePlan = false,
                onCreatePlan = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Plan created")
@Composable
private fun FocusPlanScreenWithPlanPreview() {
    FocusPlanBuilderTheme {
        Surface {
            FocusPlanScreen(
                subject = "Compose State",
                minutesText = "45",
                plan = FocusPlan(
                    subject = "Compose State",
                    minutes = 45,
                    category = durationCategory(45),
                    breakMinutes = recommendedBreak(45),
                ),
                onSubjectChange = {},
                onMinutesChange = {},
                canCreatePlan = true,
                onCreatePlan = {},
            )
        }
    }
}