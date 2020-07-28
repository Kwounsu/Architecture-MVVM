package com.example.architecturemvvm.room

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase(){

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase {
            // if the INSTANCE is not null, then return it. If it's null, then create the database.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { PopulateDbAsyncTask(it).execute() }
            }
        }

        private class PopulateDbAsyncTask(db: NoteDatabase) :
            AsyncTask<Void?, Void?, Void?>() {
            private val noteDao: NoteDao = db.noteDao()
            override fun doInBackground(vararg voids: Void?): Void? {
                noteDao.insert(Note(1, "Title 1", "Description 1", 1))
                noteDao.insert(Note(2, "Title 2", "Description 2", 2))
                noteDao.insert(Note(3, "Title 3", "Description 3", 3))
                return null
            }
        }
    }
}