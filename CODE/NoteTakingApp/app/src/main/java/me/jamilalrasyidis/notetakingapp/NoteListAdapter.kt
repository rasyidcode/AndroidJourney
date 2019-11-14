package me.jamilalrasyidis.notetakingapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteListAdapter(
    private val notes: ArrayList<Note>,
    private val onNoteListener: OnNoteListener
) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note_list, parent, false), onNoteListener)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    class ViewHolder(v: View, private val onNoteListener: OnNoteListener) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var title: TextView = v.findViewById(R.id.tv_note_title)
        private var timestamp: TextView = v.findViewById(R.id.tv_note_timestamp)

        init {
            v.setOnClickListener(this)
        }

        fun bind(note: Note) {
            title.text = note.title
            timestamp.text = note.timestamp
        }

        override fun onClick(p0: View?) {
            Log.d(TAG, "onClick: $adapterPosition")
            onNoteListener.onNoteClick(adapterPosition)
        }
    }

    interface OnNoteListener {
        fun onNoteClick(position: Int)
    }

    companion object {
        private const val TAG = "NotesListAdapter"
    }
}