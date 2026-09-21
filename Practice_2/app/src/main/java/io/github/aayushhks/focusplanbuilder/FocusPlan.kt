package io.github.aayushhks.focusplanbuilder

/** The lowest and highest study length the app will accept, in minutes. */
const val MIN_STUDY_MINUTES = 10
const val MAX_STUDY_MINUTES = 180

/** A study plan built from input the user has already been validated. */
data class FocusPlan(
    val subject: String,
    val minutes: Int,
    val category: String,
    val breakMinutes: Int,
)

/** Groups a study length into a readable category. */
fun durationCategory(minutes: Int): String = when {
    minutes < 10 -> "Invalid"
    minutes <= 29 -> "Quick review"
    minutes <= 60 -> "Focused session"
    else -> "Extended session"
}

/** Returns how many minutes of break a study length earns. */
fun recommendedBreak(minutes: Int): Int = when {
    minutes <= 29 -> 5
    minutes <= 60 -> 10
    else -> 15
}