package com.example.architecturemvvm.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.architecturemvvm.room.Note
import com.example.architecturemvvm.room.NoteDao
import com.example.architecturemvvm.room.NoteDatabase


class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNote()

    fun insert(note: Note?) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note?) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note?) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNotesAsyncTask(noteDao).execute()
    }

    private class InsertNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Note?, Void?, Void?>() {
        override fun doInBackground(vararg params: Note?): Void? {
            params[0]?.let { noteDao.insert(it) }
            return null
        }

    }

    private class UpdateNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Note?, Void?, Void?>() {
        override fun doInBackground(vararg notes: Note?): Void? {
            notes[0]?.let { noteDao.update(it) }
            return null
        }

    }

    private class DeleteNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Note?, Void?, Void?>() {
        override fun doInBackground(vararg notes: Note?): Void? {
            notes[0]?.let { noteDao.delete(it) }
            return null
        }

    }

    private class DeleteAllNotesAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg voids: Void?): Void? {
            noteDao.deleteAllNotes()
            return null
        }

    }
}