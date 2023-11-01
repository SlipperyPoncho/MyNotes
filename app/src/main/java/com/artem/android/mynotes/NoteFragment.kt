package com.artem.android.mynotes

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.util.UUID

private const val ARG_NOTE_ID = "note_id"

class NoteFragment: Fragment() {
    private lateinit var note: Note
    private lateinit var titleField: EditText
    private lateinit var textField: EditText

    private val noteViewModel: NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        note = Note()
        val noteId: UUID = arguments?.getSerializable(ARG_NOTE_ID) as UUID
        noteViewModel.loadNote(noteId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.note_fragment, container, false)
        titleField = view.findViewById(R.id.note_detail_title) as EditText
        textField = view.findViewById(R.id.note_detail_text) as EditText
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        noteViewModel.noteLiveData.observe(
            viewLifecycleOwner,
            Observer {
                note -> note?.let {
                    this.note = note
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                note.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                note.text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        titleField.addTextChangedListener(titleWatcher)
        textField.addTextChangedListener(textWatcher)
    }

    override fun onStop() {
        super.onStop()
        noteViewModel.saveNote(note)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_note -> {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Confirm delete")
                builder.setMessage("Are you sure you want to delete this note?")
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener {
                        dialog, _ -> run {
                        noteViewModel.deleteNote(note)
                        parentFragmentManager.popBackStack()
                        dialog.cancel()
                    }
                })
                builder.setNegativeButton("No", DialogInterface.OnClickListener {
                        dialog, _ -> dialog.cancel()
                })
                val alert: AlertDialog = builder.create()
                alert.show()
                true
            } else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI() {
        titleField.setText(note.title)
        textField.setText(note.text)
    }

    companion object {
        fun newInstance(noteId: UUID): NoteFragment {
            val args = Bundle().apply { putSerializable(ARG_NOTE_ID, noteId) }
            return NoteFragment().apply { arguments = args }
        }
    }
}