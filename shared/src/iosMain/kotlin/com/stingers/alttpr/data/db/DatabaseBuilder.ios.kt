package com.stingers.alttpr.data.db

import androidx.room3.Room
import androidx.room3.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val fileManager = NSFileManager.defaultManager
    val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
    val documentDirectory = urls.firstOrNull() as? NSURL ?: throw IllegalStateException("Document directory not found")
    val appDir = documentDirectory.URLByAppendingPathComponent("alttpr") ?: throw IllegalStateException("App directory URL error")
    appDir.path?.let { p ->
        if (!fileManager.fileExistsAtPath(p)) {
            fileManager.createDirectoryAtPath(p, withIntermediateDirectories = true, attributes = null, error = null)
        }
    }
    val dbFileUrl = appDir.URLByAppendingPathComponent("alttpr.db") ?: throw IllegalStateException("Database file URL error")
    val path = dbFileUrl.path ?: throw IllegalStateException("Database path error")
    return Room.databaseBuilder<AppDatabase>(
        name = path
    )
}
