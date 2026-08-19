package com.stingers.alttpr.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

actual fun createPrefsDataStore(fileName: String): DataStore<Preferences> = createPrefsDataStore(
    producePath = {
        val userHome = System.getProperty("user.home") ?: "."
        val appDir = File(userHome, ".alttpr")
        if (!appDir.exists()) appDir.mkdirs()
        File(appDir, fileName).absolutePath
    }
)
