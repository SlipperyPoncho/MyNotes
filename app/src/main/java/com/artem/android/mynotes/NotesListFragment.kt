package com.artem.android.mynotes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.UUID

class NotesListFragment: Fragment() {

    interface Callbacks { fun onNoteSelected(noteId: UUID) }

    private var callbacks: Callbacks? = null
    private lateinit var notesRecyclerView: RecyclerView
    private var adapter: NoteAdapter? = NoteAdapter(emptyList())

    private val notesListViewModel: NotesListViewModel by lazy {
        ViewModelProvider(this)[NotesListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.notes_list_fragment, container, false)
        notesRecyclerView = view.findViewById(R.id.note_recycler_view) as RecyclerView
        notesRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                notesRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val columnCount = notesRecyclerView.width/360
                notesRecyclerView.layoutManager = GridLayoutManager(context, columnCount)
            }
        })
        notesRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesListViewModel.noteListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                notes -> notes?.let { updateUI(notes) }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(notes: List<Note>) {
        adapter = NoteAdapter(notes)
        notesRecyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.notes_list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_note -> {
                val note = Note()
                notesListViewModel.addNote(note)
                callbacks?.onNoteSelected(note.id)
                true
            } else -> return super.onOptionsItemSelected(item)
        }
    }

    private inner class NoteHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var note: Note
        private val titleTextView: TextView = itemView.findViewById(R.id.note_title)
        private val noteTextView: TextView = itemView.findViewById(R.id.note_text)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(note: Note) {
            this.note = note
            titleTextView.text = this.note.title
            noteTextView.text = this.note.text
        }

        override fun onClick(v: View?) {
            callbacks?.onNoteSelected(note.id)
        }
    }

    private inner class NoteAdapter(var notes: List<Note>): RecyclerView.Adapter<NoteHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
            val view = layoutInflater.inflate(R.layout.list_item_note, parent, false)
            return NoteHolder(view)
        }

        override fun getItemCount(): Int {
            return notes.size
        }

        override fun onBindViewHolder(holder: NoteHolder, position: Int) {
            val note = notes[position]
            holder.bind(note)
        }

    }

    companion object {
        fun newInstance() : NotesListFragment {
            return NotesListFragment()
        }
    }
}