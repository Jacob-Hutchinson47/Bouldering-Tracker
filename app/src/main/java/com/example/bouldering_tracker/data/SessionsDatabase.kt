package com.example.bouldering_tracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Session::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SessionsDatabase : RoomDatabase() {
    abstract fun sessionsDao(): SessionsDao
    companion object {
        @Volatile
        private var Instance: SessionsDatabase? = null
        fun getDatabase(context: Context): SessionsDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SessionsDatabase::class.java, "sessions")
                    .build().also { Instance = it }
            }
        }
    }
}