package com.artem.android.mynotes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.artem.android.mynotes.Note

@Database(entities = [Note::class], version = 1)
@TypeConverters(com.artem.android.mynotes.database.TypeConverters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}