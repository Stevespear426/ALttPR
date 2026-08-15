package com.stingers.alttpr.data.db

import androidx.room3.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
