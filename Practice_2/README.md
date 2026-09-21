# Focus Plan Builder

**Name:** Aayush Kumar
**Assignment:** Coding Assignment 2 — Focus Plan Builder
**Package:** `io.github.aayushhks.focusplanbuilder`

## Description

A single-screen Android application that turns a study subject and an amount of
available time into a focused study plan. The user types what they are studying
and how many minutes they have. Once both values are valid, the Create plan
button becomes enabled and produces a card showing the subject, the session
length, a duration category, a recommended break, and a one-sentence summary.

Durations run from 10 to 180 minutes. Anything shorter, longer, non-numeric or
empty leaves the button disabled, and the app never crashes on bad input.

| Duration | Category | Break |
|---|---|---|
| 10–29 minutes | Quick review | 5 minutes |
| 30–60 minutes | Focused session | 10 minutes |
| 61–180 minutes | Extended session | 15 minutes |

Built with Kotlin and Jetpack Compose (Material 3). No XML layouts, Views or
Fragments.

## Running it

1. Open the `Focus_Plan_Builder` folder in Android Studio.
2. Let Gradle sync finish.
3. Start an emulator from Device Manager (developed on Medium Phone, API 36).
4. Press Run, or from a terminal in the project folder:

```bash
./gradlew installDebug
adb shell am start -n io.github.aayushhks.focusplanbuilder/.MainActivity
```

## Screenshot

![Focus Plan Builder showing a completed plan](screenshots/plan-created.png)

## State and recomposition

**Which composable owns the application state?** `FocusPlanRoute` owns it. It
holds `subject`, `minutesText` and `plan`, runs the validation, and passes plain
values plus callbacks down to `FocusPlanScreen`, which owns nothing of its own
and only draws what it is handed.

**Why are the text-field values stored as `String` rather than `Int`?** An
`OutlinedTextField` gives back exactly what the user typed, including an empty
field or a half-finished number. An `Int` cannot represent `""` or a value still
being typed, so keeping the raw `String` keeps the field and the state in step,
and the app converts only at the moment it needs a number.

**Why is `toIntOrNull()` safer than `toInt()` here?** `toInt()` throws a
`NumberFormatException` on anything that is not a number, so typing `abc` or
clearing the field would crash the app. `toIntOrNull()` returns `null` instead,
and the validation simply treats `null` as invalid input.

**What state change causes the button to be recomposed?** `canCreatePlan` is
derived from `subject` and `minutesText` rather than stored separately. Editing
either field writes to its `MutableState`, Compose marks the code that read it
as out of date, and the button recomposes with a new `enabled` value.

**What does `rememberSaveable` preserve that a local variable would not?** A
plain variable is reset on every recomposition, and `remember` survives
recomposition but not Activity recreation. `rememberSaveable` writes to the
saved instance state bundle, so both text fields are still filled in after the
emulator is rotated.
