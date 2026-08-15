package com.stingers.alttpr.repository.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun createDataStore(): DataStore<Preferences> {
    val context: Context = object : KoinComponent {
        val ctx: Context by inject()
    }.ctx
    return createDataStore(
        producePath = { context.filesDir.resolve("rom_prefs.preferences_pb").absolutePath }
    )
}
