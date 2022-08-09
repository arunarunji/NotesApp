package com.example.notesappfragment.repository

import androidx.lifecycle.LiveData
import com.example.notesappfragment.entities.Notes
import com.example.notesappfragment.noteDao.NoteDao

class NotesRepository(private val notesDao: NoteDao) {


    val allNotes: LiveData<List<Notes>> = notesDao.getAllNotes()

    suspend fun insert(note: Notes) {
        notesDao.insert(note)
    }

    suspend fun delete(id: Int) {
        notesDao.delete(id)
    }

    suspend fun update(note: Notes) {
        notesDao.update(note)
    }
}
