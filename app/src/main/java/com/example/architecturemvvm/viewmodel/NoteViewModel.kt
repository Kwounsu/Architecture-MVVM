package com.example.architecturemvvm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.architecturemvvm.repository.NoteRepository
import com.example.architecturemvvm.room.Note
import com.example.architecturemvvm.room.NoteDatabase

class NoteViewModel(application: Application): AndroidViewModel(application) {
    private val repository: NoteRepository

    val allNotes: LiveData<List<Note>>

    init {
        val notesDao = NoteDatabase.getInstance(application).noteDao()
        repository = NoteRepository(notesDao)
        allNotes = repository.allNotes
    }

    fun insert(note: Note) {
        repository.insert(note)
    }

    fun update(note: Note) {
        repository.update(note)
    }

    fun delete(note: Note) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }
}