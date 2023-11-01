package com.artem.android.mynotes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.artem.android.mynotes.Note
import java.util.UUID

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getNotes(): LiveData<List<Note>>
    @Query("SELECT * FROM note WHERE id=(:id)")
    fun getNote(id: UUID): LiveData<Note?>
    @Insert
    fun addNote(note: Note)
    @Update
    fun updateNote(note: Note)
    @Delete
    fun deleteNote(note: Note)
}