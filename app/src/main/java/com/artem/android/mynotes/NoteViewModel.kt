package com.artem.android.mynotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import java.util.UUID

class NoteViewModel: ViewModel() {

    private val notesRepository = NotesRepository.get()
    private val noteIdLiveData = MutableLiveData<UUID>()

    var noteLiveData: LiveData<Note?> = noteIdLiveData.switchMap {
        noteId -> notesRepository.getNote(noteId)
    }

    fun loadNote(noteId: UUID) {
        noteIdLiveData.value = noteId
    }

    fun saveNote(note: Note) {
        notesRepository.updateNote(note)
    }

    fun deleteNote(note: Note) {
        notesRepository.deleteNote(note)
    }
}