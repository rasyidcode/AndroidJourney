package me.jamilalrasyidis.notetakingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.jamilalrasyidis.utils.VerticalSpacingItemDecorator

class NotesListActivity : AppCompatActivity(), NoteListAdapter.OnNoteListener {

    private lateinit var notesListRecyclerView: RecyclerView

    private val notes: ArrayList<Note> = arrayListOf()
    private lateinit var adapter: NoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        initNoteList()
        insertFakeNotes()

        setSupportActionBar(findViewById(R.id.toolbar_notes))
        title = "Notes"
    }

    private fun initNoteList() {
        notesListRecyclerView = findViewById(R.id.rv_note_list)
        adapter = NoteListAdapter(notes, this@NotesListActivity)

        notesListRecyclerView.layoutManager = LinearLayoutManager(this@NotesListActivity)
        notesListRecyclerView.addItemDecoration(VerticalSpacingItemDecorator(10))
        notesListRecyclerView.adapter = adapter
    }

    private fun insertFakeNotes() {
        for (i in 0..999) {
            notes.add(Note("title #$i", "content #$i", "Nov 2019"))
        }
        adapter.notifyDataSetChanged()
    }

    override fun onNoteClick(position: Int) {
        Log.d(TAG, "onNoteClick: $position")
    }

    companion object {
        private const val TAG = "NotesListActivity"
    }
}
