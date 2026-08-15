package com.stingers.alttpr.data.db

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val objectWithContext = object : KoinComponent {
        val context: Context by inject()
    }
    val appContext = objectWithContext.context.applicationContext
    val dbFile = appContext.getDatabasePath("alttpr.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
