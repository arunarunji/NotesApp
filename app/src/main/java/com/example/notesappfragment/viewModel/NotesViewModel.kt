package com.example.notesappfragment.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notesappfragment.database.NoteDataBase
import com.example.notesappfragment.entities.Notes
import com.example.notesappfragment.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotesViewModel(application: Application) : AndroidViewModel(application) {


    private val repo: NotesRepository


    init {
        val dao = NoteDataBase.getDatabase(application).getNotesDao()
        repo = NotesRepository(dao)


    }


    fun getNotes(): LiveData<List<Notes>> = repo.allNotes



    fun deleteNote(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repo.delete(id)

    }

    fun updateNote(note: Notes) = viewModelScope.launch(Dispatchers.IO) {
        repo.update(note)
    }

    fun addNotes(note: Notes) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(note)

    }
}