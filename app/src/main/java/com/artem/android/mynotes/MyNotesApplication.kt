package com.artem.android.mynotes

import android.app.Application

class MyNotesApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        NotesRepository.init(this)
    }
}