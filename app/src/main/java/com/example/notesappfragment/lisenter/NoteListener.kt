package com.example.notesappfragment.lisenter

import android.view.View
import com.example.notesappfragment.entities.Notes

interface NoteListener {

    fun onNoteClicked(view: View?, note: Notes, position: Int)

    fun onNoteLongClicked(view: View?, note: Notes, position: Int)
}