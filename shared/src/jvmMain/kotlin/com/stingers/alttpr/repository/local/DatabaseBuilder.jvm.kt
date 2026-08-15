package com.stingers.alttpr.repository.local

import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val userHome = System.getProperty("user.home") ?: "."
    val appDir = File(userHome, ".alttpr")
    if (!appDir.exists()) appDir.mkdirs()
    val dbFile = File(appDir, "alttpr.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
}
