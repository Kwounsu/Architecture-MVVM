package com.example.architecturemvvm.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.architecturemvvm.R
import com.example.architecturemvvm.room.Note
import com.example.architecturemvvm.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val ADD_NOTE_REQUEST = 1
    private val EDIT_NOTE_REQUEST = 2

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add Note
        button_addNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        // RecyclerView
        val adapter = NoteAdapter()
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        // create an instance
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        noteViewModel.allNotes.observe(this, Observer { notes ->
            notes?.let { adapter.setNotes(it) }
        })

        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.getNoteAt(viewHolder.adapterPosition)?.let { noteViewModel.delete(it) }
                    Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                }
            }
        ).attachToRecyclerView(recyclerview)

        adapter.setOnItemClickListener(object :
            NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                intent.putExtra("EXTRA_ID", note.id)
                intent.putExtra("EXTRA_TITLE", note.title)
                intent.putExtra("EXTRA_DESCRIPTION", note.description)
                intent.putExtra("EXTRA_PRIORITY", note.priority)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title = data!!.getStringExtra("EXTRA_TITLE")
            val description = data.getStringExtra("EXTRA_DESCRIPTION")
            val priority = data.getIntExtra("EXTRA_PRIORITY", 1)
            val note = Note(0,title, description, priority)
            noteViewModel.insert(note)
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            val id = data!!.getIntExtra("EXTRA_ID", -1)
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val title = data.getStringExtra("EXTRA_TITLE");
            val description = data.getStringExtra("EXTRA_DESCRIPTION")
            val priority = data.getIntExtra("EXTRA_PRIORITY", 1)
            val note = Note(id, title, description, priority)
            noteViewModel.update(note)
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes_item -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }
}