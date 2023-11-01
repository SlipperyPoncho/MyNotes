package com.artem.android.mynotes

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.artem.android.mynotes.database.NoteDatabase
import java.lang.IllegalStateException
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "note-database"

class NotesRepository private constructor(context: Context) {

    private val database: NoteDatabase = Room.databaseBuilder(
        context.applicationContext,
        NoteDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val noteDao = database.noteDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getNotes(): LiveData<List<Note>> = noteDao.getNotes()
    fun getNote(id: UUID): LiveData<Note?> = noteDao.getNote(id)
    fun addNote(note: Note) {
        executor.execute { noteDao.addNote(note) }
    }
    fun updateNote(note: Note) {
        executor.execute { noteDao.updateNote(note) }
    }
    fun deleteNote(note: Note) {
        executor.execute {noteDao.deleteNote(note) }
    }
    companion object {
        private var INSTANCE: NotesRepository? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = NotesRepository(context)
            }
        }

        fun get(): NotesRepository {
            return INSTANCE ?: throw IllegalStateException("NotesRepository must be initialized")
        }
    }
}