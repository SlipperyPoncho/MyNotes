package com.artem.android.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.UUID

class MainActivity : AppCompatActivity(), NotesListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = NotesListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onNoteSelected(noteId: UUID) {
        val fragment = NoteFragment.newInstance(noteId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}