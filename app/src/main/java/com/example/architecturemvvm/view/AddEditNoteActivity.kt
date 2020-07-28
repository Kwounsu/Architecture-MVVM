package com.example.architecturemvvm.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import com.example.architecturemvvm.R

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var editTextTitle: TextView
    private lateinit var editTextDescription: TextView
    private lateinit var numberPickerPriority: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextDescription = findViewById(R.id.edit_text_description)
        numberPickerPriority = findViewById(R.id.number_picker_priority)

        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        val intent = intent

        if (intent.hasExtra("EXTRA_ID")) {
            title = "Edit Note"
            editTextTitle.text = intent.getStringExtra("EXTRA_TITLE")
            editTextDescription.text = intent.getStringExtra("EXTRA_DESCRIPTION")
            numberPickerPriority.value = intent.getIntExtra("EXTRA_PRIORITY", 1)
        } else {
            title = "Add Note"
        }
    }

    private fun saveNote() {
        val title = editTextTitle.text.toString()
        val description = editTextDescription.text.toString()
        val priority = numberPickerPriority.value

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        data.putExtra("EXTRA_TITLE", title)
        data.putExtra("EXTRA_DESCRIPTION", description)
        data.putExtra("EXTRA_PRIORITY", priority)
        val id = intent.getIntExtra("EXTRA_ID", -1)
        if (id != -1) {
            data.putExtra("EXTRA_ID", id)
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}