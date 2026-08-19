package com.stingers.alttpr.repository.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun createPrefsDataStore(fileName: String): DataStore<Preferences> {
    val context: Context = object : KoinComponent {
        val ctx: Context by inject()
    }.ctx
    return createPrefsDataStore(
        producePath = { context.filesDir.resolve(fileName).absolutePath }
    )
}
