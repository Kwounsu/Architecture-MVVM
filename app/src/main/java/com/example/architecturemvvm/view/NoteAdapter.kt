package com.example.architecturemvvm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.architecturemvvm.R
import com.example.architecturemvvm.room.Note
import androidx.recyclerview.widget.ListAdapter

class NoteAdapter: ListAdapter<Note, NoteAdapter.ViewHolder>(DiffCallback()) {

    private var notes = emptyList<Note>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        var textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        var textViewPriority: TextView = itemView.findViewById(R.id.text_view_priority)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(notes[position])
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
//            return oldItem == newItem
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description &&
                    oldItem.priority == newItem.priority
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.note_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteAdapter.ViewHolder, position: Int) {
        val current = notes[position]
        holder.textViewPriority.text = current.priority.toString()
        holder.textViewTitle.text = current.title
        holder.textViewDescription.text = current.description
    }

    internal fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int): Note? {
        return notes[position]
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}