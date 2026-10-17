package io.github.aayushhks.campuseventexplorer.share

import android.content.Context
import android.content.Intent
import io.github.aayushhks.campuseventexplorer.R
import io.github.aayushhks.campuseventexplorer.data.CampusEvent
import io.github.aayushhks.campuseventexplorer.data.shareText

/**
 * Opens the Android share sheet for [event]. The intent only names an action
 * and a MIME type, never a target app, so it is implicit and the system lists
 * every app that can receive plain text.
 */
fun Context.shareEvent(event: CampusEvent) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, event.title)
        putExtra(Intent.EXTRA_TEXT, event.shareText())
    }
    startActivity(Intent.createChooser(sendIntent, getString(R.string.share_chooser_title)))
}
