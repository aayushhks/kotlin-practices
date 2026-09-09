package com.example.myapplication

import androidx.annotation.StringRes

/**
 * One of the six dimensions that separate a mobile application from a stationary one.
 *
 * All user-facing copy is stored as string resources so that the text can be
 * translated or reworded without touching Kotlin code.
 */
data class MobilityDimension(
    @param:StringRes val titleRes: Int,
    @param:StringRes val constraintRes: Int,
    @param:StringRes val implicationRes: Int,
)

/** The ordered list the user pages through with the Previous and Next controls. */
val mobilityDimensions: List<MobilityDimension> = listOf(
    MobilityDimension(
        titleRes = R.string.dimension_input_title,
        constraintRes = R.string.dimension_input_constraint,
        implicationRes = R.string.dimension_input_implication,
    ),
    MobilityDimension(
        titleRes = R.string.dimension_screen_title,
        constraintRes = R.string.dimension_screen_constraint,
        implicationRes = R.string.dimension_screen_implication,
    ),
    MobilityDimension(
        titleRes = R.string.dimension_lifecycle_title,
        constraintRes = R.string.dimension_lifecycle_constraint,
        implicationRes = R.string.dimension_lifecycle_implication,
    ),
    MobilityDimension(
        titleRes = R.string.dimension_context_title,
        constraintRes = R.string.dimension_context_constraint,
        implicationRes = R.string.dimension_context_implication,
    ),
    MobilityDimension(
        titleRes = R.string.dimension_usage_title,
        constraintRes = R.string.dimension_usage_constraint,
        implicationRes = R.string.dimension_usage_implication,
    ),
    MobilityDimension(
        titleRes = R.string.dimension_privacy_title,
        constraintRes = R.string.dimension_privacy_constraint,
        implicationRes = R.string.dimension_privacy_implication,
    ),
)
