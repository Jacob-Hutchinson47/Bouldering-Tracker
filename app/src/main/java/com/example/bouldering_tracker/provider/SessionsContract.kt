package com.example.bouldering_tracker.provider

import android.net.Uri

object SessionsContract {
    const val AUTHORITY = "com.example.bouldering_tracker.provider"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")

    object Sessions {
        const val PATH_SESSIONS = "Sessions"
        val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SESSIONS)

        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_SESSIONS"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_SESSIONS"

        const val COLUMN_ID = "id"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_DATE = "date"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_CLIMBS = "climbs"
    }
}