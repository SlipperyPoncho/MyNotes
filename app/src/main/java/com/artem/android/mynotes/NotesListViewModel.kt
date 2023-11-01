package com.artem.android.mynotes

import androidx.lifecycle.ViewModel

class NotesListViewModel : ViewModel() {

    private val notesRepository = NotesRepository.get()
    val noteListLiveData = notesRepository.getNotes()

    fun addNote(note: Note) {
        notesRepository.addNote(note)
    }
}